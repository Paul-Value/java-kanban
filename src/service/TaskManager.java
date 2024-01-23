package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subTasks;
    int counter = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    private int generateId() {
        return ++counter;
    }

    // Методы Task
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task, int id, TaskStatus status) {
        task.setId(id);
        task.setStatus(status);
        tasks.put(id, task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    // Методы Epic
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic, int id) {
        Epic saved = epics.get(id);
        epic.setStatus(saved.getStatus());
        epic.setSubTasks(saved.getSubTasks());
        epic.setId(saved.getId());
        epics.put(id, epic);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    private void calculateEpicStatus(Epic epic) {
        int statusDone = 0;

        for (Subtask subtask : epic.getSubTasks()) {
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                statusDone++;
            }
        }
        if (statusDone == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        return epic.getSubTasks();
    }

    //Методы Subtask
    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
        }
    }

    public Subtask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Subtask createSubTask(Subtask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.getSubTasks().add(subTask);
        return subTask;
    }

    public void updateSubTask(int subTaskId, Subtask subtask) {
        subtask.setId(subTaskId);
        Subtask oldSubtask = subTasks.get(subTaskId);
        subTasks.put(subTaskId, subtask);
        Epic epic = oldSubtask.getEpic();
        epic.getSubTasks().remove(oldSubtask);
        epic.getSubTasks().add(subtask);
        calculateEpicStatus(epic);
    }

    public void deleteSubTaskById(int id) {
        Subtask removeSubtask = subTasks.remove(id);

        Epic epic = removeSubtask.getEpic();
        Epic epicSaved = epics.get(epic.getId());

        epicSaved.getSubTasks().remove(removeSubtask);
        calculateEpicStatus(epicSaved);
    }
}
