package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String name, TaskStatus status, String description) {

        super(name, status, description);
    }

    public Epic(int id, String name, TaskStatus status, String description) {

        super(id, name, status, description);
    }

    public Epic(Epic epic) {

        super(epic.getId(), epic.getName(), epic.getStatus(), epic.getDescription());
        this.subTasks = epic.getSubTasks();
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        /*for (int sub : this.subTasks) {
            sub = subTasks.get(this.subTasks.indexOf(sub));
        }*/
        this.subTasks = new ArrayList<>(subTasks);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' + "," + '\n' +
                "subTasks=" + subTasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
