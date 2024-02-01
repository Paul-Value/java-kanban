package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    public void ShouldReturnTaskManager(){
        TaskManager taskManager = Manager.getDefaultTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager1 = new InMemoryTaskManager(historyManager);

        assertNotNull(taskManager);
        assertNotEquals(taskManager, taskManager1);
    }
}