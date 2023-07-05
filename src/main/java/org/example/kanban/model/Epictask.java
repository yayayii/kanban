package org.example.kanban.model;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class Epictask extends Task {
    private Set<Subtask> subtasks = new HashSet<>();


    @Builder(builderMethodName = "epictaskBuilder")
    public Epictask(long id, String name, String description, String status, Set<Subtask> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epictask epictask = (Epictask) o;
        return Objects.equals(subtasks, epictask.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
