package service.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtaskHandler extends Handler {
    private final TaskManager taskManager;

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    ErrorHandler errorHandler = new ErrorHandler();

    public SubtaskHandler(TaskManager taskManager) {
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
            List<Subtask> subtasks = taskManager.getSubTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");
            if (pathParts.length == 3) {
                Subtask subtask = taskManager.getSubTaskById(Integer.parseInt(pathParts[2]));
                if (subtask == null) {
                    //throw new NoSuchObjectException("Задачи с таким ID не существует:" + pathParts[2]);
                    sendResponse(exchange, "Подзадачи с таким ID не существует:" + pathParts[2], 404);
                }
                //String jTask = gson.toJson(task);
                sendResponse(exchange, gson.toJson(subtask), 200);
            } else if (pathParts.length == 2) {
                sendResponse(exchange, gson.toJson(subtasks), 200);
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
            List<Subtask> subtasks = taskManager.getSubTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(streamReader, Subtask.class);
            if (pathParts.length == 3) {
                taskManager.updateTask(subtask);
                sendResponse(exchange, "Подзадача обновлена", 200);
            } else if (pathParts.length == 2) {
                taskManager.createTask(subtask);
                sendResponse(exchange, "Подзадача создана", 201);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        try {
            List<Subtask> subtasks = taskManager.getSubTasks();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            if (pathParts.length == 3) {
                taskManager.deleteSubTaskById(Integer.parseInt(pathParts[2]));
                sendResponse(exchange, "Подзадача удалена", 200);
            } else if (pathParts.length == 2) {
                taskManager.deleteAllSubTasks();
                sendResponse(exchange, "Все подзадачи удалены", 200);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
