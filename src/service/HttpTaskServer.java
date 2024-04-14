package service;

import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    //private TaskManager httpTaskManager;


    public HttpTaskServer(TaskManager taskManager) {
        //this.httpTaskManager = taskManager;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            createHandlers(taskManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHandlers(TaskManager taskManager) {
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandle(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        Task task = taskManager.createTask(new Task("Task with time 22", TaskStatus.NEW, "time task 22",
                LocalDateTime.of(2024, 4, 3, 19, 30), Duration.ofMinutes(30)));
        Epic epic1 = taskManager.createEpic(new Epic("Epic with time", TaskStatus.NEW, "time epic"));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Subtask with time", TaskStatus.NEW,
                "time subtask", LocalDateTime.of(2024, 4, 4, 15, 20),
                Duration.ofMinutes(20), epic1.getId()));
        httpTaskServer.start();
    }
}
