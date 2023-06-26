package org.example.kanban.controller;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class KanbanController {
    private static final int PORT = 8080;

    private final HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);


    public KanbanController() throws IOException {
        TaskHandler tasksHandler = new TaskHandler();
        server.createContext(ApiPath.TASK.getPath(), tasksHandler::createContextForTasks);
    }


    public void start() {
        log.info("Server was launched on port {}", PORT);
        server.start();
    }
}
