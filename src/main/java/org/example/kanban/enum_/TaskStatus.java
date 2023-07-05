package org.example.kanban.enum_;

public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String statusName;

    TaskStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static boolean isValid(String status) {
        for (TaskStatus taskStatus: TaskStatus.values()) {
            if (taskStatus.getStatusName().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
