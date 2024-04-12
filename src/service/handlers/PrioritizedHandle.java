package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandle implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public PrioritizedHandle(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            sendResponse(exchange, gson.toJson(prioritizedTasks), 200);
        } else {
            sendResponse(exchange, "Endpoint not exist", 404);
        }
    }

    private void sendResponse(HttpExchange exchange, String responseText, int code) throws IOException {
        exchange.getRequestHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, responseText.getBytes().length);
        exchange.getResponseBody().write(responseText.getBytes());
        exchange.getResponseBody().close();
    }
}
