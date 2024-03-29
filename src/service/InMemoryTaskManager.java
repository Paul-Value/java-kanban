package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    final Map<Integer, Epic> epics;
    final Map<Integer, Subtask> subTasks;
    final HistoryManager historyManager;
    int counter = 0;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    private int generateId() {
        return ++counter;
    }

    // Методы Task
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
        //return tasks.values();
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            System.out.println("Задачи с таким id не существует");
        }
        tasks.put(task.getId(), task);
        historyManager.add(task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }


    // Методы Epic
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) == null) {
            System.out.println("Эпика с таким id не существует");
        }
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        historyManager.add(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic removedEpic = epics.remove(id);
        for (Integer sub : removedEpic.getSubTasks()) {
            subTasks.remove(sub);
        }
        historyManager.remove(id);
    }

     @Override
     public void calculateEpicStatus(Epic epic) {
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

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        List<Subtask> subtasksFromEpic = new ArrayList<>();
        for (int i = 0; i < epic.getSubTasks().size(); i++) {
            subtasksFromEpic.add(subTasks.get(epic.getSubTasks().get(i)));
        }
        return subtasksFromEpic;
    }

    //Методы Subtask
    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            calculateEpicStatus(epic);
        }
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = subTasks.get(id);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Subtask createSubTask(Subtask subTask) {
        if (epics.get(subTask.getEpicId()) == null) {
            System.out.println("Такого Эпика не существует, невозможно создать Сабтаск");
            return null;
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic relatedEpic = epics.get(subTask.getEpicId());
        relatedEpic.getSubTasks().add(subTask.getId());
        calculateEpicStatus(relatedEpic);
        return subTask;
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        //subtask.setId();
        int epicId = subTasks.get(subTask.getId()).getEpicId();
        subTask.setEpic(epicId);
        if (subTasks.get(subTask.getId()) == null || epics.get(epicId) == null) {
            System.out.println("Невозможно обновить Сабтаск");
        }
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        calculateEpicStatus(epic);
        historyManager.add(subTask);
    }

    @Override
    public void deleteSubTaskById(int id) {
        Subtask removeSubtask = subTasks.remove(id);

        Epic epic = epics.get(removeSubtask.getEpicId());
        epic.getSubTasks().remove((Integer) removeSubtask.getId());
        calculateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getAll();
    }
}
