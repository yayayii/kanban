package org.example.kanban.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.model.Subtask;
import org.example.kanban.service.SubtaskServiceImpl;

@Slf4j
public class SubtaskHandler extends TaskHandler {
    public SubtaskHandler() {
        super(new SubtaskServiceImpl(), ApiPath.SUBTASK.getPath(), Subtask.class);
    }
}
