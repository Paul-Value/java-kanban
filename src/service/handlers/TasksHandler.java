package service.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TasksHandler extends Handler {
    private final TaskManager taskManager;

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    //Gson gson = new Gson();
    ErrorHandler errorHandler = new ErrorHandler();

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                sendResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");
            if (pathParts.length == 3) {
                Task task = taskManager.getTaskById(Integer.parseInt(pathParts[2]));
                if (task == null) {
                    //throw new NoSuchObjectException("Задачи с таким ID не существует:" + pathParts[2]);
                    sendResponse(exchange, "Задачи с таким ID не существует:" + pathParts[2], 404);
                }
                //String jTask = gson.toJson(task);
                sendResponse(exchange, gson.toJson(task), 200);
            } else if (pathParts.length == 2) {
                sendResponse(exchange, gson.toJson(tasks), 200);
            } else {
                //throw new NoSuchObjectException("Неверный эндпоинт");
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(streamReader, Task.class);
            if (pathParts.length == 3) {
                taskManager.updateTask(task);
                sendResponse(exchange, "Задача обновлена", 200);
            } else if (pathParts.length == 2) {
                Task createTask = taskManager.createTask(task);
                sendResponse(exchange, "Задача создана", 201);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            if (pathParts.length == 3) {
                taskManager.deleteTaskById(Integer.parseInt(pathParts[2]));
                sendResponse(exchange, "Задача удалена", 200);
            } else if (pathParts.length == 2) {
                taskManager.deleteAllTasks();
                sendResponse(exchange, "Все задачи удалены", 200);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
