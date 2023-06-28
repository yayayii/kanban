package org.example.kanban.controller;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Epictask;
import org.example.kanban.service.EpictaskService;
import org.example.kanban.service.KanbanServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class EpictaskHandler extends TaskHandler {
    private final EpictaskService service = new KanbanServiceImpl();


    @Override
    protected void handlePostMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);

        if (ApiPath.EPICTASK.getPath().equals(path)) {
            try {
                Epictask epictask = gson.fromJson(JsonParser.parseString(body).toString(), Epictask.class);
                log.info("KanbanController - Creating epictask: {}", epictask);
                service.createEpictask(epictask);
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

    @Override
    protected void handleGetMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        String response;

        if (ApiPath.EPICTASK.getPath().equals(path) && query != null && (query.contains("id="))) {
            try {
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - getting epictask by id = {}", id);
                response = gson.toJson(service.getEpictaskById(id));
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

    @Override
    protected void handlePutMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        String query = httpExchange.getRequestURI().getQuery();

        if (ApiPath.EPICTASK.getPath().equals(path)) {
            try {
                Epictask newEpictask = gson.fromJson(JsonParser.parseString(body).toString(), Epictask.class);
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - updating epictask: {} / id = {}", newEpictask, id);
                service.updateEpictask(newEpictask, id);
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

    @Override
    protected void handleDeleteMethod() throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();

        if (ApiPath.EPICTASK.getPath().equals(path) && query != null && (query.contains("id="))) {
            try {
                long id = Long.parseLong(query.substring("id=".length()));
                log.info("KanbanController - deleting epictask by id = {}", id);
                service.deleteEpictaskById(id);
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