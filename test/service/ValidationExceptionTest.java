package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationExceptionTest {
    File file;

    {
        try {
            file = File.createTempFile("file", "tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

    @Test
    public void testException() {
        assertThrows(ValidationException.class, () -> {
            Task task4 = fileBackedTaskManager1.createTask(null);
        }, "Деление на ноль должно приводить к исключению");
    }
}