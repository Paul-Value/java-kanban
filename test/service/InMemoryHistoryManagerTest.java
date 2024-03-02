package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты Истории")
class InMemoryHistoryManagerTest {

    @Test
    void addInHistory() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Epic epic = new Epic(1,"name", TaskStatus.NEW, "description");
        historyManager.add(epic);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void removeNode() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Epic epic = new Epic(1,"name", TaskStatus.NEW, "description");
        historyManager.add(epic);
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
}