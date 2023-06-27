package org.example.kanban.controller;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class KanbanController {
    private final int port;
    private final HttpServer server;


    public KanbanController(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        TaskHandler tasksHandler = new TaskHandler();
        server.createContext(ApiPath.TASK.getPath(), tasksHandler::createContextForTasks);
    }


    public void start() {
        log.info("Server was launched on port {}", port);
        server.start();
    }
}
