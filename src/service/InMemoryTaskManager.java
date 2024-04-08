package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    final Map<Integer, Epic> epics;
    final Map<Integer, Subtask> subTasks;
    final HistoryManager historyManager;
    int counter = 0;

    TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        tasks.keySet().forEach(historyManager::remove);
        tasks.values().forEach(prioritizedTasks::remove);
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
        if (!isVerified(task)) {
            return null;
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addToPriorityTasks(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            System.out.println("Задачи с таким id не существует");
        }
        tasks.put(task.getId(), task);
        historyManager.add(task);
        addToPriorityTasks(task);
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
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
        epics.keySet().forEach(historyManager::remove);
        subTasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subTasks.values().forEach(prioritizedTasks::remove);
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
        addToPriorityTasks(epic);
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
        removedEpic.getSubTasks().forEach(subTasks::remove);
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
        } else if (statusDone != 0 && statusDone < epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public void calculateEpicTime(Epic epic) {
        if (epic.getSubTasks() == null) {
            return;
        }

        Optional<LocalDateTime> epicStartTime = epic.getSubTasks().stream()
                .map(subTasks::get)
                .map(Subtask::getStartTime)
                .filter(startTime -> startTime != null)
                .min(LocalDateTime::compareTo);
        epicStartTime.ifPresent(epic::setStartTime);

        Optional<LocalDateTime> epicEndTime = epic.getSubTasks().stream()
                .map(subTasks::get)
                .map(Task::getEndTime)
                .filter(endTime -> endTime != null)
                .max(LocalDateTime::compareTo);
        epicEndTime.ifPresent(epic::setEndTime);
        if (epicStartTime.isPresent() && epicEndTime.isPresent()) {
            epic.setDuration(Duration.between(epicStartTime.get(), epicEndTime.get()));
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        return epic.getSubTasks().stream()
                .map(subTasks::get)
                .collect(Collectors.toList());
    }

    //Методы Subtask
    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.keySet().forEach(historyManager::remove);
        subTasks.values().forEach(prioritizedTasks::remove);
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubTasks().clear();
            calculateEpicStatus(epic);
        });
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
        if (!isVerified(subTask)) {
            return null;
        }
        if (epics.get(subTask.getEpicId()) == null) {
            System.out.println("Такого Эпика не существует, невозможно создать Сабтаск");
            return null;
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic relatedEpic = epics.get(subTask.getEpicId());
        relatedEpic.getSubTasks().add(subTask.getId());
        calculateEpicStatus(relatedEpic);
        calculateEpicTime(relatedEpic);
        addToPriorityTasks(subTask);
        return subTask;
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        int epicId = subTasks.get(subTask.getId()).getEpicId();
        subTask.setEpic(epicId);
        if (subTasks.get(subTask.getId()) == null || epics.get(epicId) == null) {
            System.out.println("Невозможно обновить Сабтаск");
        }
        addToPriorityTasks(subTask);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        calculateEpicStatus(epic);
        calculateEpicTime(epic);
        historyManager.add(subTask);
    }

    @Override
    public void deleteSubTaskById(int id) {
        prioritizedTasks.remove(subTasks.get(id));
        Subtask removeSubtask = subTasks.remove(id);

        Epic epic = epics.get(removeSubtask.getEpicId());
        epic.getSubTasks().remove((Integer) removeSubtask.getId());
        calculateEpicStatus(epic);
        calculateEpicTime(epic);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getAll();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void addToPriorityTasks(Task task) {

        if (task.getStartTime() != null && isVerified(task)) {
            Task original;
            switch (task.getType()) {
                case TASK:
                    original = tasks.get(task.getId());
                    break;
                case EPIC:
                    original = epics.get(task.getId());
                    break;
                case SUBTASK:
                    original = subTasks.get(task.getId());
                    break;
                default:
                    original = null;
            }
            if (original != null && !task.getStartTime().equals(original.getStartTime())) {
                prioritizedTasks.remove(original);
            }
            prioritizedTasks.add(task);
        }
    }

    @Override
    public List<Task> getAll() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    public boolean isVerified(Task taskForVerification) {
        if (taskForVerification == null) {
            throw new ValidationException("Ошибка валидации");
        }
        List<Task> allTasks = getAll();
        List<Task> notVerifiedTasks = allTasks.stream()
                .filter(task -> task.getStartTime() != null &&
                        taskForVerification.getStartTime() != task.getStartTime() &&
                        taskForVerification.getDuration() != task.getDuration())
                .filter(task -> !(taskForVerification.getEndTime().isBefore(task.getStartTime()) ||
                        taskForVerification.getStartTime().isAfter(task.getEndTime())))
                .collect(Collectors.toList());
        return notVerifiedTasks.isEmpty();
    }
}
