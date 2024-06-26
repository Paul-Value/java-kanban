package service;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Map<Integer, Task> backedTasks = new HashMap<>();
    private final File file;
    public static final String TASK_CSV = "task.csv";
    public static final String NULL_STRING = "null";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void init() {
        loadFromFile();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.init();
        return manager;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task createTask(Task task) {
        Task backedTask = super.createTask(task);
        save();
        return backedTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    // Методы Epic
    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic backedEpic = super.createEpic(epic);
        save();
        return backedEpic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void calculateEpicStatus(Epic epic) {
        super.calculateEpicStatus(epic);
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        return super.getSubtasksFromEpic(epic);
    }

    //Методы Subtask
    @Override
    public List<Subtask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }

    @Override
    public Subtask createSubTask(Subtask subTask) {
        Subtask backedSubTask = super.createSubTask(subTask);
        save();
        return backedSubTask;
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, UTF_8))) {
            writer.write("id,type,name,status,description,epic,startTime,duration");
            writer.newLine();

            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }

            for (Subtask subtask : getSubTasks()) {
                writer.write(toString(subtask));
                writer.newLine();
            }

            for (Epic epic : getEpics()) {
                writer.write((toString(epic)));
                writer.newLine();
            }
            writer.newLine();

            writer.write(toString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription() + "," + task.getEpicId() + "," + task.getStartTime() + "," + task.getDuration();
    }

    private Task fromString(String value) {
        final String[] columns = value.split(",");
        int id = Integer.parseInt(columns[0]);
        String name = columns[2];
        String description = columns[4];
        TaskStatus status = TaskStatus.valueOf(columns[3]);
        LocalDateTime startTime = null;
        Duration duration = null;

        if (!columns[6].equals(NULL_STRING) && !columns[7].equals(NULL_STRING)) {
            startTime = LocalDateTime.parse(columns[6]);
            duration = Duration.parse(columns[7]);
        }

        TaskType type = TaskType.valueOf(columns[1]);
        Task task = null;
        switch (type) {
            case TASK:
                task = new Task(id, name, status, description, startTime, duration);
                break;

            case SUBTASK:
                int epicId = Integer.parseInt(columns[5]);
                task = new Subtask(id, name, status, description, startTime, duration, epicId);
                break;

            case EPIC:
                Epic epic = new Epic(id, name, status, description);
                List<Integer> epicSubtasks = new ArrayList<>();
                //Epic epic = (Epic) task;
                for (Map.Entry<Integer, Subtask> e : subTasks.entrySet()) {
                    if (id == e.getValue().getEpicId()) {
                        epicSubtasks.add(e.getValue().getId());
                    }
                }
                epic.setSubTasks(epicSubtasks);
                calculateEpicTime(epic);
                return epic;
        }
        return task;
    }

    static String toString(HistoryManager manager) {
        //String sb = "";
        List<String> ids = manager.getAll().stream().map(task -> String.valueOf(task.getId())).collect(Collectors.toList());
        return String.join(",", ids);
    }

    private void loadFromFile() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);
                backedTasks.put(task.getId(), task);
                final int id = task.getId();

                if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    subTasks.put(id, (Subtask) task);
                } else if (task.getType() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                }

                if (maxId < id) {
                    maxId = id;
                }
            }

            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                return;
            }
            String[] historyIds = line.split(",");
            Arrays.stream(historyIds)
                    .map(id -> backedTasks.get(Integer.parseInt(id)))
                    .forEach(historyManager::add);

            backedTasks.values().forEach(this::addToPriorityTasks);


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
        // генератор
        counter = maxId;

    }
}
