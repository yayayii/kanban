package org.example.kanban.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ErrorResponse;
import org.example.kanban.service.KanbanService;
import org.example.kanban.service.KanbanServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ApiHandler {
    private final KanbanService service = new KanbanServiceImpl();
    protected final Gson gson = new Gson();
    protected HttpExchange httpExchange;


    //method handling
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

    //path handling
    private void handleGetMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String response;

        //GET
        if (ApiPath.API.getPath().equals(path)) {
            try {
                log.info("KanbanController - getting all tasks");
                response = gson.toJson(service.getAllTasks());
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

    private void handleDeleteMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        //DELETE
        if (ApiPath.API.getPath().equals(path)) {
            try {
                log.info("KanbanController - deleting all tasks");
                service.deleteAllTasks();
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
