package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends Handler {
    private final TaskManager taskManager;
    Gson gson = new Gson();
    ErrorHandler errorHandler = new ErrorHandler();

    public EpicHandler(TaskManager taskManager) {
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
            List<Epic> epics = taskManager.getEpics();
            Epic epic;
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            if (pathParts.length == 4) {
                if (!pathParts[3].equals("subTasks")) {
                    sendResponse(exchange, "Неверный эндпоинт", 404);
                }
                epic = taskManager.getEpicById(Integer.parseInt(pathParts[2]));
                if (epic == null) {
                    sendResponse(exchange, "Эпика с таким ID не существует:" + pathParts[2], 404);
                }
                List<Subtask> subtasks = taskManager.getSubtasksFromEpic(epic);
                sendResponse(exchange, gson.toJson(subtasks), 200);
            } else if (pathParts.length == 3) {
                epic = taskManager.getEpicById(Integer.parseInt(pathParts[2]));
                if (epic == null) {
                    //throw new NoSuchObjectException("Задачи с таким ID не существует:" + pathParts[2]);
                    sendResponse(exchange, "Эпика с таким ID не существует:" + pathParts[2], 404);
                }
                //String jTask = gson.toJson(task);
                sendResponse(exchange, gson.toJson(epic), 200);
            } else if (pathParts.length == 2) {
                sendResponse(exchange, gson.toJson(epics), 200);
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
            List<Epic> epics = taskManager.getEpics();
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(streamReader, Epic.class);
            if (pathParts.length == 3) {
                taskManager.updateEpic(epic);
                sendResponse(exchange, "Эпик обновлен", 200);
            } else if (pathParts.length == 2) {
                taskManager.createEpic(epic);
                sendResponse(exchange, "Эпик создан", 201);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        try {
            String endpoint = exchange.getRequestURI().getPath();
            String[] pathParts = endpoint.split("/");

            if (pathParts.length == 3) {
                taskManager.deleteEpicById(Integer.parseInt(pathParts[2]));
                sendResponse(exchange, "Эпик удален", 200);
            } else if (pathParts.length == 2) {
                taskManager.deleteAllEpics();
                sendResponse(exchange, "Все эпики удалены", 200);
            } else {
                sendResponse(exchange, "Неверный эндпоинт", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
