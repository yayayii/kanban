package org.example.kanban.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ErrorResponse;
import org.example.kanban.service.TaskService;
import org.example.kanban.service.TaskServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ApiHandler {
    protected final Gson gson = new Gson();
    protected final String expectedPath;
    protected final TaskService service;
    protected HttpExchange httpExchange;


    public ApiHandler() {
        expectedPath = ApiPath.API.getPath();
        service = new TaskServiceImpl();
    }

    public ApiHandler(TaskService service, String expectedPath) {
        this.expectedPath = expectedPath;
        this.service = service;
    }


    public void createContext(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    handleGetMethod();
                    break;
                case "DELETE":
                    handleDeleteMethod();
                    break;
                default:
                    sendError("Wrong request method", HttpURLConnection.HTTP_BAD_METHOD);
            }
        } finally {
            httpExchange.close();
        }
    }

    protected void handleGetMethod() throws IOException {
        String actualPath = httpExchange.getRequestURI().getPath();
        String response;

        //GET /api : 200 + body
        if (expectedPath.equals(actualPath)) {
            try {
                log.info("KanbanController - getting all kanban tasks");
                response = gson.toJson(service.getAllKanbanTasks());
            } catch (Exception e) {
                sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                return;
            }
        } else {
            sendError("Wrong path", HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }

        sendJson(response, HttpURLConnection.HTTP_OK);
    }

    protected void handleDeleteMethod() throws IOException {
        String actualPath = httpExchange.getRequestURI().getPath();

        //DELETE /api : 200
        if (expectedPath.equals(actualPath)) {
            try {
                log.info("KanbanController - deleting all kanban tasks");
                service.deleteAllKanbanTasks();
            } catch (Exception e) {
                sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                return;
            }
        } else {
            sendError("Wrong path", HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }

        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }


    protected void sendJson(String object, int statusCode) throws IOException {
        byte[] json = object.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(statusCode, json.length);
        httpExchange.getResponseBody().write(json);
    }

    protected void sendError(String errorMessage, int errorStatusCode) throws IOException {
        log.error(errorMessage);
        sendJson(gson.toJson(
                        new ErrorResponse(
                                errorMessage,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        )),
                errorStatusCode
        );
        httpExchange.sendResponseHeaders(errorStatusCode, 0);
    }
}
