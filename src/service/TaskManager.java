package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    // Методы Task
    List<Task> getTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    // Методы Epic
    List<Epic> getEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    void calculateEpicStatus(Epic epic);

    List<Subtask> getSubtasksFromEpic(Epic epic);

    //Методы Subtask
    List<Subtask> getSubTasks();

    void deleteAllSubTasks();

    Subtask getSubTaskById(int id);

    Subtask createSubTask(Subtask subTask);

    void updateSubTask(Subtask subTask);

    void deleteSubTaskById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    List<Task> getAll();
}
