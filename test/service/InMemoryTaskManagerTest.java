package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class InMemoryTaskManagerTest {
    TaskManager taskManager;
    @BeforeEach
    public void beforeEach(){
        taskManager = Manager.getDefaultTaskManager();
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

}