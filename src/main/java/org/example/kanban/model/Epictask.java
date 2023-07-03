package org.example.kanban.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Epictask extends Task {
    private Set<Subtask> subtasks = new HashSet<>();
}
