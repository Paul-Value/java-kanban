package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
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
    public Collection<Task> getTasks() {
        return new ArrayList<>(tasks.values());
        //return tasks.values();
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

    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            System.out.println("Задачи с таким id не существует");
        }
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    // Методы Epic
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) == null) {
            System.out.println("Эпика с таким id не существует");
        }
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public void deleteEpicById(int id) {
        Epic removedEpic = epics.remove(id);
        for (Integer sub : removedEpic.getSubTasks()) {
            subTasks.remove(sub);
        }
    }

    private void calculateEpicStatus(Epic epic) {
        int statusDone = 0;

        if (epic.getSubTasks() == null) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        for (Integer subId : epic.getSubTasks()) {
            if (subTasks.get(subId).getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (subTasks.get(subId).getStatus() == TaskStatus.DONE) {
                statusDone++;
            }
        }
        if (statusDone != 0 && statusDone == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        List<Subtask> subtasksFromEpic = new ArrayList<>();
        for (int i = 0; i < epic.getSubTasks().size(); i++) {
            subtasksFromEpic.add(subTasks.get(epic.getSubTasks().get(i)));
        }
        return subtasksFromEpic;
    }

    //Методы Subtask
    public Collection<Subtask> getSubTasks() {
        return subTasks.values();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            calculateEpicStatus(epic);
        }
    }

    public Subtask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Subtask createSubTask(Subtask subTask) {
        if (epics.get(subTask.getEpic()) == null) {
            System.out.println("Такого Эпика не существует, невозможно создать Сабтаск");
            return null;
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic relatedEpic = epics.get(subTask.getEpic());
        relatedEpic.getSubTasks().add(subTask.getId());
        calculateEpicStatus(relatedEpic);
        return subTask;
    }

    public void updateSubTask(Subtask subTask) {
        //subtask.setId();
        int epicId = subTasks.get(subTask.getId()).getEpic();
        subTask.setEpic(epicId);
        if (subTasks.get(subTask.getId()) == null || epics.get(epicId) == null) {
            System.out.println("Невозможно обновить Сабтаск");
        }
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpic());
        calculateEpicStatus(epic);
    }

    public void deleteSubTaskById(int id) {
        Subtask removeSubtask = subTasks.remove(id);

        Epic epic = epics.get(removeSubtask.getEpic());
        epic.getSubTasks().remove((Integer) removeSubtask.getId());
        calculateEpicStatus(epic);
    }
}
