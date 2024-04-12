package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends Handler {
    private final TaskManager taskManager;
    Gson gson = new Gson();
    ErrorHandler errorHandler = new ErrorHandler();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().equals("GET")) {
                List<Task> history = taskManager.getHistory();
                sendResponse(exchange, gson.toJson(history), 200);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
