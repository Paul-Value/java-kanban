package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandle extends Handler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();
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
