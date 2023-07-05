package org.example.kanban.model;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class Subtask extends Task {
    private long epictaskId;


    @Builder(builderMethodName = "subtaskBuilder")
    public Subtask(long id, String name, String description, String status, long epictaskId) {
        super(id, name, description, status);
        this.epictaskId = epictaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epictaskId == subtask.epictaskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epictaskId);
    }
}
