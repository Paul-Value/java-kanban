package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class InMemoryTaskManagerTest {
    TaskManager taskManager;
    @BeforeEach
    public void beforeEach(){
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    public void shouldAddNewTaskAndGetById() {
        Task task = taskManager.createTask(new Task("Test addNewTask", TaskStatus.NEW, "Test addNewTask description"));

        Task taskById = taskManager.getTaskById(task.getId());

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);

        assertEquals(task,taskById);
    }

    @Test
    public void shouldAddNewEpicAndGetById() {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", TaskStatus.NEW, "Test addNewEpic description"));
        Epic epic1 = taskManager.createEpic(new Epic("Test addNewEpic1", TaskStatus.NEW, "Test addNewEpic description1"));

        Task epicById = taskManager.getEpicById(epic.getId());

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertEquals(2,taskManager.getEpics().size());

        assertEquals(epic,epicById);
    }

    @Test
    void addToPriorityTaskAndNoIntersect() {
        Epic epic = taskManager.createEpic(new Epic("Epic", TaskStatus.NEW, "Epic"));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask with time", TaskStatus.NEW,
                "time subtask", LocalDateTime.of(2024, 4, 4, 15, 20),
                Duration.ofMinutes(20), epic.getId()));
        Subtask subtask2 = taskManager.createSubTask(new Subtask("Subtask2 with time", TaskStatus.NEW,
                "time subtask2", LocalDateTime.of(2024, 4, 4, 20, 31),
                Duration.of(50, ChronoUnit.MINUTES), epic.getId()));
        Subtask subtask3 = taskManager.createSubTask(new Subtask("Subtask not add to priority", TaskStatus.NEW,
                "time subtask3", LocalDateTime.of(2024, 4, 4, 19, 30),
                Duration.of(120, ChronoUnit.MINUTES), epic.getId()));
        assertEquals(2, taskManager.getPrioritizedTasks().size());


    }

}