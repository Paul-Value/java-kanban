package service.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PrioritizedHandle extends Handler {
    private final TaskManager taskManager;

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    ErrorHandler errorHandler = new ErrorHandler();

    public PrioritizedHandle(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().equals("GET")) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendResponse(exchange, gson.toJson(prioritizedTasks), 200);
            } else {
                sendResponse(exchange, "Endpoint not exist", 404);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
