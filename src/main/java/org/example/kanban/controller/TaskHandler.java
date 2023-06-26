package org.example.kanban.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ErrorResponse;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.service.KanbanService;
import org.example.kanban.service.KanbanServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class TaskHandler {
    private final KanbanService kanbanService = new KanbanServiceImpl();
    private final Gson gson = new Gson();
    private HttpExchange httpExchange;


    //method handling
    public void createContextForTasks(HttpExchange httpExchange) throws IOException {
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
    private void handlePostMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);

        //POST + "/tasks" + request body
        if (ApiPath.TASK.getPath().equals(path)) {
            Task task = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
            try {
                log.info("KanbanController - Creating task: {}", task);
                kanbanService.createTask(task);
            } catch (ValidationException e) {
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

    private void handleGetMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        String response;

        if (ApiPath.TASK.getPath().equals(path)) {
            //GET + "/tasks?id="
            if (query != null && (query.contains("id="))) {
                try {
                    long id = Integer.parseInt(query.substring("id=".length()));
                    log.info("KanbanController - getting task by id = {}", id);
                    response = gson.toJson(kanbanService.getTaskById(id));
                } catch (NumberFormatException | ValidationException e) {
                    sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                    return;
                } catch (Exception e) {
                    sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                    return;
                }
                //GET + "/tasks"
            } else {
                try {
                    log.info("KanbanController - getting all tasks");
                    response = gson.toJson(kanbanService.getAllTasks());
                } catch (Exception e) {
                    sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                    return;
                }
            }
        } else {
            sendError("Wrong path", HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }

        sendJson(response, HttpURLConnection.HTTP_OK);
    }

    private void handlePutMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        String query = httpExchange.getRequestURI().getQuery();

        //PUT + "/tasks?id=" + request body
        if (ApiPath.TASK.getPath().equals(path)) {
            Task newTask = gson.fromJson(JsonParser.parseString(body).toString(), Task.class);
            try {
                long id = Integer.parseInt(query.substring("id=".length()));
                log.info("KanbanController - updating task: {} / id = {}", newTask, id);
                kanbanService.updateTask(newTask, id);
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

    private void handleDeleteMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();

        if (ApiPath.TASK.getPath().equals(path)) {
            //DELETE + "/tasks?id="
            if (query != null && (query.contains("id="))) {
                try {
                    long id = Integer.parseInt(query.substring("id=".length()));
                    log.info("KanbanController - deleting task by id = {}", id);
                    kanbanService.deleteTaskById(id);
                } catch (NumberFormatException | ValidationException e) {
                    sendError("Wrong request: " + e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
                    return;
                } catch (Exception e) {
                    sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                    return;
                }
                //DELETE + "/tasks"
            } else {
                try {
                    log.info("KanbanController - deleting all tasks");
                    kanbanService.deleteAllTasks();
                } catch (Exception e) {
                    sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                    return;
                }
            }
        } else {
            sendError("Wrong path", HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }

        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }


    private void sendJson(String object, int statusCode) throws IOException {
        byte[] json = object.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(statusCode, json.length);
        httpExchange.getResponseBody().write(json);
    }

    private void sendError(String errorMessage, int errorStatusCode) throws IOException {
        log.error(errorMessage);
        sendJson(gson.toJson(new ErrorResponse(errorMessage, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))), errorStatusCode);
        httpExchange.sendResponseHeaders(errorStatusCode, 0);
    }
}
