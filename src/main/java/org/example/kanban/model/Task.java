package org.example.kanban.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {
    private long id;
    private String name;
    private String description;
    private String status;
}
