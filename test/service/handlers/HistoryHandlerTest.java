package service.handlers;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryHandlerTest {
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

    @DisplayName("должен возвращать корректный JSON при GET-запросе")
    @Test
    void shouldReturnCorrectJsonOnGetRequest() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            taskManager.createTask(new Task("Task", TaskStatus.NEW, "task"));
            taskManager.getTaskById(1);

            URI url = URI.create("http://localhost:8080/history");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jTask = "[{\"id\":1,\"name\":\"Task\",\"status\":\"NEW\",\"description\":\"task\"}]";
            assertEquals(jTask, response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }

    }

}