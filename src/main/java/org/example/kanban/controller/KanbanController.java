package org.example.kanban.controller;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.service.EpictaskServiceImpl;
import org.example.kanban.service.SubtaskServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class KanbanController {
    private final int port;
    private final HttpServer server;


    public KanbanController(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 0);

        ApiHandler apiHandler = new ApiHandler();
        ApiHandler taskHandler = new TaskHandler();
        ApiHandler epictaskHandler = new TaskHandler(
                new EpictaskServiceImpl(), ApiPath.EPICTASK.getPath(), Epictask.class
        );
        ApiHandler subtaskHandler = new TaskHandler(
                new SubtaskServiceImpl(), ApiPath.SUBTASK.getPath(), Subtask.class
        );
        server.createContext(ApiPath.API.getPath(), apiHandler::createContext);
        server.createContext(ApiPath.TASK.getPath(), taskHandler::createContext);
        server.createContext(ApiPath.EPICTASK.getPath(), epictaskHandler::createContext);
        server.createContext(ApiPath.SUBTASK.getPath(), subtaskHandler::createContext);
    }


    public void start() {
        log.info("Server was launched on port {}", port);
        server.start();
    }
}
