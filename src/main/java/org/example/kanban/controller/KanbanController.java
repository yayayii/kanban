package org.example.kanban.controller;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.repository.factory.InMemoryRepositoryFactory;
import org.example.kanban.repository.factory.RepositoryFactory;
import org.example.kanban.service.EpictaskService;
import org.example.kanban.service.SubtaskService;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class KanbanController {
    private final int port;
    private final HttpServer server;


    public KanbanController(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 0);

        RepositoryFactory repository = InMemoryRepositoryFactory.getInstance();
        ApiHandler apiHandler = new ApiHandler(repository);
        ApiHandler taskHandler = new TaskHandler(repository);
        ApiHandler epictaskHandler = new TaskHandler(
                new EpictaskService(repository), ApiPath.EPICTASK.getPath(), Epictask.class
        );
        ApiHandler subtaskHandler = new TaskHandler(
                new SubtaskService(repository), ApiPath.SUBTASK.getPath(), Subtask.class
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
