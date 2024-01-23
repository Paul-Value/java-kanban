package model;

public class Subtask extends Task {
    Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);

        this.epic = epic;
    }

    public Subtask(String name, TaskStatus status, String description, Epic epic) {
        super(name, status, description);

        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epic ='" + epic.getName() + '\'' +
                '}';
    }
}
