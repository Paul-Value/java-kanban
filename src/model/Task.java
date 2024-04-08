package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private TaskStatus status;
    private String description;

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    private LocalDateTime startTime;
    private LocalDateTime endTime = getEndTime();
    private Duration duration;

    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.status = task.status;
        this.description = task.description;
        this.startTime = task.startTime;
        this.duration = task.duration;
        this.endTime = getEndTime();
    }

    public Task(int id, String name, TaskStatus status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        //this.startTime = LocalDateTime.now();
        //this.duration = Duration.ofMinutes(0);
        //this.endTime = getEndTime();
    }

    public Task(String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.status = status;
        this.description = description;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(int id, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String name, TaskStatus status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
        //this.startTime = LocalDateTime.now();
        //this.duration = Duration.ofMinutes(0);
    }

    /*public Task(String name) {
        this.name = name;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }*/

    public Integer getEpicId() {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }

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

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        assert duration != null;
        endTime = startTime.plus(duration);
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }
}
