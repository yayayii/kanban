package org.example.kanban.controller;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.service.TaskService;
import org.example.kanban.service.TaskServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class TaskHandler extends ApiHandler {
    protected Class<? extends Task> taskType;


    public TaskHandler() {
        super(new TaskServiceImpl(), ApiPath.TASK.getPath());
        taskType = Task.class;
    }

    public TaskHandler(TaskService service, String expectedPath, Class<? extends Task> taskType) {
        super(service, expectedPath);
        this.taskType = taskType;
    }


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

    protected void handlePostMethod() throws IOException {
        String actualPath = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        String response;

        //POST /api/tasks(subtasks/epictasks) + body : 201
        if (expectedPath.equals(actualPath)) {
            try {
                Task task = gson.fromJson(JsonParser.parseString(body).toString(), taskType);
                log.info("KanbanController - Creating task: {}", task);
                response = gson.toJson(service.createTask(task));
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

        sendJson(response, HttpURLConnection.HTTP_CREATED);
    }

    @Override
    protected void handleGetMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        String response;

        if (expectedPath.equals(path)) {
            //GET /api/tasks(subtasks/epictasks) : 200 + body
            if (query == null) {
                try {
                    log.info("KanbanController - getting all tasks");
                    response = gson.toJson(service.getAllTasks());
                } catch (Exception e) {
                    sendError("Unknown error: " + e.getMessage(), HttpURLConnection.HTTP_SERVER_ERROR);
                    return;
                }
            //GET /api/tasks(subtasks/epictasks)?id= : 200 + body
            } else if (query.contains("id=")) {
                try {
                    long id = Long.parseLong(query.substring("id=".length()));
                    log.info("KanbanController - getting task by id = {}", id);
                    response = gson.toJson(service.getTaskById(id), taskType);
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
        String response;

        //PUT /api/tasks?id= + body : 200
        if (expectedPath.equals(path) && query != null && (query.contains("id="))) {
            try {
                Task newTask = gson.fromJson(JsonParser.parseString(body).toString(), taskType);
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - updating task: {} / id = {}", newTask, id);
                response = gson.toJson(service.updateTask(newTask, id));
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

        sendJson(response, HttpURLConnection.HTTP_OK);
    }

    @Override
    protected void handleDeleteMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();

        //DELETE /api/tasks(subtasks/epictasks)?id= : 200
        if (expectedPath.equals(path) && query != null && (query.contains("id="))) {
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
