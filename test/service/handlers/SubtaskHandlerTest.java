package service.handlers;

import model.Epic;
import model.Subtask;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskHandlerTest {
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;
    Epic epic;
    Subtask subTask1;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        epic = taskManager.createEpic(new Epic("Test", TaskStatus.NEW, "Test"));
        subTask1 = taskManager.createSubTask(new Subtask("Подзадача1", TaskStatus.NEW, "Описание1", epic.getId()));


    }

    @AfterEach
    void end() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectSubtaskOnGET() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonSubtask = "[{\"epicId\":1,\"id\":2,\"name\":\"Подзадача1\",\"status\":\"NEW\"," +
                    "\"description\":\"Описание1\"}]";
            assertEquals(200, response.statusCode(), "Неверный код ответа");
            assertEquals(jsonSubtask, response.body(), "Сервер ответил неверным JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса возникла ошибка.");
        }
    }

}