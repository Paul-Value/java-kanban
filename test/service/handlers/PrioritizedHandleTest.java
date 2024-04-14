package service.handlers;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHandleTest {
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void end() {
        httpTaskServer.stop();
    }

    @Test
    void shouldReturnCorrectPrioritizedListOnGetRequest() {
        LocalDateTime basicLDT = LocalDateTime.of(2024, 1, 1, 11, 11);
        try (HttpClient client = HttpClient.newHttpClient()) {
            Task task2 = taskManager.createTask(new Task("Task with time 22", TaskStatus.NEW, "time task 22",
                    LocalDateTime.of(2024, 4, 3, 19, 30), Duration.ofMinutes(30)));
            Task task3 = taskManager.createTask(new Task("Task not added to priority", TaskStatus.NEW, "time task3",
                    LocalDateTime.of(2024, 4, 3, 18, 30), Duration.ofMinutes(70)));

            URI url = URI.create("http://localhost:8080/prioritized");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jTask = "[{\"id\":1,\"name\":\"Task with time 22\",\"status\":\"NEW\"," +
                    "\"description\":\"time task 22\",\"startTime\":\"2024-04-03T19:30:00\",\"endTime\":\"2024-04-03T20:00:00\",\"duration\":\"PT30M\"}]";
            assertEquals(jTask, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }

    }

}