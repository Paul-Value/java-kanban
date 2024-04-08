package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты Истории")
class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        Epic epic = new Epic(1, "name", TaskStatus.NEW, "description");
        historyManager.add(epic);
    }
    @Test
    void addInHistory() {
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void removeNode() {
        Task task = new Task(2,"NewTask", TaskStatus.NEW, "description");
        historyManager.add(task);
        Task task2 = new Task(3,"NewTask2", TaskStatus.NEW, "description");
        historyManager.add(task2);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История пустая.");
        assertEquals(3, history.size());
        historyManager.remove(3);
        final List<Task> history2 = historyManager.getAll();
        assertEquals(2, history2.size(), "Не удалилась задача");
    }

    @Test
    void noDuplicateTasksInHistory() {
        TaskManager tm = Managers.getDefaultTaskManager();
        Task task = tm.createTask(new Task("NewTask", TaskStatus.NEW, "description"));
        Task taskById = tm.getTaskById(task.getId());
        Task task1 = tm.createTask(new Task("NewTask", TaskStatus.NEW, "description"));
        Task taskById1 = tm.getTaskById(task1.getId());
        Task taskById2 = tm.getTaskById(task1.getId());
        assertEquals(2, tm.getHistory().size());

    }
}