package org.example.kanban.controller;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.service.KanbanServiceImpl;
import org.example.kanban.service.TaskService;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class TaskHandler extends ApiHandler {
    private final TaskService service = new KanbanServiceImpl();


    @Override
    public void createContext(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        try {
            switch (httpExchange.getRequestMethod()) {
                case "POST":
                    handlePostMethod();
                    break;
                case "GET":
                    handleGetMethod();
                    break;
                case "PUT":
                    handlePutMethod();
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
    protected void handlePostMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);

        if (ApiPath.TASK.getPath().equals(path)) {
            try {
                Task task = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
                log.info("KanbanController - Creating task: {}", task);
                service.createTask(task);
            } catch (ValidationException | JsonSyntaxException e) {
                sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            } catch (Exception e) {
                sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                return;
            }
        } else {
            sendError("Wrong path", HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }

        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, 0);
    }

    protected void handleGetMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        String response;

        if (ApiPath.TASK.getPath().equals(path) && query != null && (query.contains("id="))) {
            try {
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - getting task by id = {}", id);
                response = gson.toJson(service.getTaskById(id));
            } catch (NumberFormatException | ValidationException e) {
                sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
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

    protected void handlePutMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        String query = httpExchange.getRequestURI().getQuery();

        if (ApiPath.TASK.getPath().equals(path)) {
            try {
                Task newTask = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - updating task: {} / id = {}", newTask, id);
                service.updateTask(newTask, id);
            } catch (NumberFormatException | ValidationException | JsonSyntaxException e) {
                sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
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

    protected void handleDeleteMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();

        if (ApiPath.TASK.getPath().equals(path) && query != null && (query.contains("id="))) {
            try {
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - deleting task by id = {}", id);
                service.deleteTaskById(id);
            } catch (NumberFormatException | ValidationException e) {
                sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                return;
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
}
