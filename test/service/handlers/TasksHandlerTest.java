package service.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TasksHandlerTest {
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;
    Task task;
    Task task1;

    @BeforeEach
    void beforeAll() {
        taskManager = Managers.getDefaultTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        task = taskManager.createTask(new Task("Task with time 22", TaskStatus.NEW, "time task 22"));
        task1 = taskManager.createTask(new Task("Task 33", TaskStatus.NEW, "task 33"));
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeCorrectTaskOnGET() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonTask = "[{\"id\":1,\"name\":\"Task with time 22\",\"status\":\"NEW\"," +
                    "\"description\":\"time task 22\"}," +
                    "{\"id\":2,\"name\":\"Task 33\",\"status\":\"NEW\",\"description\":\"task 33\"}]";
            assertEquals(200, response.statusCode());
            assertEquals(jsonTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    @Test
    void shouldReturnTaskOnGETWithID() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String jTask = "{\"id\":1,\"name\":\"Task with time 22\",\"status\":\"NEW\",\"description\":\"time task 22\"}";
            assertEquals(200, response.statusCode());
            assertEquals(jTask, response.body(), "bad JSON");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    @Test
    void shouldReturnCorrectTaskOnPOST() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String post = gson.toJson(task);

            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(post, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseAns = "Задача создана";
            assertEquals(201, response.statusCode());
            assertEquals(responseAns, response.body(), "Bad Answer");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса возникла ошибка.");
        }
    }


}