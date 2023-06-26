package org.example.kanban.enum_;

public enum ApiPath {
    TASK("/api/tasks");

    private final String path;

    ApiPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
