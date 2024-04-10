package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
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
        TaskManager taskManager = Managers.getDefaultTaskManager();
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
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    TaskManager taskManager;
    Epic epicNew;


    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
        epicNew = new Epic(1, "Test Epic", TaskStatus.NEW, "Test epic");
        taskManager.createEpic(epicNew);
    }

    @Test
    void statusShouldBeNew() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        taskManager.createEpic(new Epic(1, "Test Epic", TaskStatus.IN_PROGRESS, "Test Epic description"));
        Epic epic = taskManager.getEpicById(1);
        final int epicId = epic.getId();
        Subtask subtask = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.NEW,
                "sub description", epicId));
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void statusShouldBeInProgress() {
        Subtask subtask = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.NEW,
                "sub description", epicNew.getId()));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.IN_PROGRESS,
                "sub description", epicNew.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epicNew.getStatus());
    }

    @Test
    void statusShouldBeDone() {
        Subtask subtask = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.DONE,
                "sub description", epicNew.getId()));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.DONE,
                "sub description", epicNew.getId()));
        assertEquals(TaskStatus.DONE, epicNew.getStatus());
    }

    @Test
    void statusShouldBeInProgress1() {
        Subtask subtask = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.DONE,
                "sub description", epicNew.getId()));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.NEW,
                "sub description", epicNew.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epicNew.getStatus());
    }

    @Test
    void statusShouldBeInProgress2() {
        Subtask subtask = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.IN_PROGRESS,
                "sub description", epicNew.getId()));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask", TaskStatus.IN_PROGRESS,
                "sub description", epicNew.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epicNew.getStatus());
    }
}