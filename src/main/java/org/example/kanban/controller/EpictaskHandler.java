package org.example.kanban.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.ApiPath;
import org.example.kanban.model.Epictask;
import org.example.kanban.service.EpictaskServiceImpl;

@Slf4j
public class EpictaskHandler extends TaskHandler {
    public EpictaskHandler() {
        super(new EpictaskServiceImpl(), ApiPath.EPICTASK.getPath(), Epictask.class);
    }
}
