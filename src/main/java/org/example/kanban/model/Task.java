package org.example.kanban.model;

import lombok.Data;

@Data
public class Task {
    private long id;
    private String name;
    private String description;
    private String status;
}
