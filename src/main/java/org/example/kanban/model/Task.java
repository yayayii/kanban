package org.example.kanban.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Task implements Comparable<Task> {
    private long id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime endTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Task o) {
        if (this.endTime == null) {
            return -1;
        }
        if (o.getEndTime() == null) {
            return 1;
        }
        return this.endTime.compareTo(o.getEndTime());
    }
}
