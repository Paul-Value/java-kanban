package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void ShouldReturnTaskManager(){
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager1 = new InMemoryTaskManager();

        assertNotNull(taskManager);
        assertNotEquals(taskManager, taskManager1);
    }
}