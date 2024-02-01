package model;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Manager;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubtaskTest {
    @Test
    public void shouldBeEqualWhenSameId() {
        Epic epic = new Epic("nameEpic", TaskStatus.NEW, "descriptionEpic");
        Subtask subtask = new Subtask("name", TaskStatus.NEW, "description", epic.getId());
        Subtask subtask1 = new Subtask("name1", TaskStatus.NEW, "description1", epic.getId());
        subtask1.setId(subtask.getId());
        assertEquals(subtask, subtask1);
    }

    @Test
    public void addNewSubTask() {
        TaskManager taskManager = Manager.getDefaultTaskManager();
        Epic epic = taskManager.createEpic(new Epic("nameEpic", TaskStatus.NEW, "descriptionEpic"));
        Subtask subtask = taskManager.createSubTask(new Subtask("Test addNewEpic", TaskStatus.NEW, "Test addNewEpic description", epic.getId()));
        /*final int subtaskId = taskManager.createSubTask(subtask).getId();*/

        final Subtask savedSubtask = taskManager.getSubTaskById(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");

        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");

        assertEquals(1, subtasks.size(), "Неверное количество задач.");

        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addSubTaskInHistory() {
        Epic epic = new Epic("nameEpic", TaskStatus.NEW, "descriptionEpic");
        Subtask subtask = new Subtask("Test addNewEpic", TaskStatus.NEW, "Test addNewEpic description", epic.getId());
        HistoryManager historyManager = Manager.getDefaultHistory();
        historyManager.add(subtask);
        final List<Task> history = historyManager.getAll();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}