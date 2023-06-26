package org.example.kanban.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String message;
    private final String timestamp;
}
