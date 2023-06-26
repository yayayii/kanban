package org.example.kanban;

import org.example.kanban.controller.KanbanController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KanbanController controller = new KanbanController();
        controller.start();
    }
}
