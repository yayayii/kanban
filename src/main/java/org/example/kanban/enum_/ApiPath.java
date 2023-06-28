package org.example.kanban.enum_;

public enum ApiPath {
    API("/api"),
    TASK("/api/tasks"),
    SUBTASK("/api/subtasks"),
    EPICTASK("/api/epictasks");

    private final String path;

    ApiPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
