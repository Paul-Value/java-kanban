package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, TaskStatus status, String description, int epicId) {
        super(name, status, description);

        this.epicId = epicId;
    }

    public Subtask(int subTaskId, String name, TaskStatus status, String description) {
        super(subTaskId, name, status, description);
    }

    public Subtask(Subtask subtask) {
        super(subtask.getId(),subtask.getName(), subtask.getStatus(), subtask.getDescription());

        this.epicId = subtask.getEpicId();
    }

    public Subtask(int id, String name, TaskStatus status, String description, int epicId) {
        super(id, name, status, description);

        this.epicId = epicId;
    }

    public Subtask(String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpic(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epic ='" + getEpicId() + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                ", duration=" + getDuration() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
