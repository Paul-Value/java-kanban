package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Manager;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты Эпик")
class EpicTest {

    @Test
    public void shouldBeEqualWhenSameId() {
        Epic epic = new Epic("name", TaskStatus.NEW, "description");
        Epic epic1 = new Epic("name1", TaskStatus.NEW, "description1");
        epic1.setId(epic.getId());
        assertEquals(epic, epic1);
    }

    @Test
    public void addNewEpic() {
        TaskManager taskManager = Manager.getDefaultTaskManager();
        Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW, "Test addNewEpic description");
        final int epicId = taskManager.createEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");

        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");

        assertEquals(1, epics.size(), "Неверное количество задач.");

        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpicInHistory() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW, "Test addNewTask description");
        HistoryManager historyManager = Manager.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}