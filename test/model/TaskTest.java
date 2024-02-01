package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Manager;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты по Таск")
class TaskTest {
    @Test
    public void shouldBeEqualWhenSameId() {
        Task task = new Task("name", TaskStatus.NEW, "description");
        Task task1 = new Task("name1", TaskStatus.NEW, "description1");
        task1.setId(task.getId());
        assertEquals(task, task1);
    }

    @Test
    public void addNewTask() {
        TaskManager taskManager = Manager.getDefaultTaskManager();
        Task task = new Task("Test addNewTask", TaskStatus.NEW, "Test addNewTask description");
        final int taskId = taskManager.createTask(task).getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");

        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");

        assertEquals(1, tasks.size(), "Неверное количество задач.");

        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW, "Test addNewTask description");
        HistoryManager historyManager = Manager.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}