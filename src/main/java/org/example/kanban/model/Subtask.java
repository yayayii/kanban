package org.example.kanban.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Subtask extends Task {
    private long epictaskId;
}
