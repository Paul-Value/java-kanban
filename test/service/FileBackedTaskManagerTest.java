package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileBackedTaskManagerTest {
    File file;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("text", ".temp");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле", e);
        }
    }

    @Test
    void loadFromNullFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            assertNull(reader.readLine(), "Not null");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.createTask(new Task("Проверка записи в файл", TaskStatus.NEW, "Проверка"));

        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        assertEquals("Проверка", fileBackedTaskManager1.getTasks().get(0).getDescription(),
                "Несовпадение описания Task");
    }

    @AfterEach
    void afterEach() {
        file.deleteOnExit();
    }
}