import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.FileBackedTaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) {
        // Тесты по Task
        /*TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task1 = taskManager.createTask(new Task("Делать проект 4 спринта", TaskStatus.NEW,
                "Понять как сделать проект 4 спринта!!!"));
        System.out.println("Создали задачу: " + task1);
        System.out.println("______________________________________");

        System.out.println("++++++++++++++++++++++++++++++++++++++");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++");*/

        File file = new File(FileBackedTaskManager.TASK_CSV);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.createTask(new Task("Проверка записи в файл", TaskStatus.NEW, "Проверка"));

        fileBackedTaskManager.createEpic(new Epic("Проверка Эпика", TaskStatus.NEW, "ПроверОчка"));

        fileBackedTaskManager.createSubTask(new Subtask("Проверка сабтаск", TaskStatus.NEW, "сабтаск", 2));

        Task task = fileBackedTaskManager.getTaskById(1);
        System.out.println(task);

        Epic epic = fileBackedTaskManager.getEpicById(2);
        System.out.println(epic);

        Subtask subtask = fileBackedTaskManager.getSubTaskById(3);

        System.out.println(fileBackedTaskManager.getHistory());

        System.out.println("---------------------------------------------------------");
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(file);
        System.out.println("Проверка чтения из файла" + fileBackedTaskManager1.getSubTasks());

        fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Проверка чтения из файла" + fileBackedTaskManager1.getSubTasks());

        System.out.println("---------------------------------------------------------");
        System.out.println("Проверка расчета времени у Эпика");

        Task task1 = fileBackedTaskManager1.createTask(new Task(4, "Task with time", TaskStatus.NEW, "time task",
                LocalDateTime.of(2024, 4, 3, 20, 30), Duration.ofMinutes(30)));
        System.out.println(task1);

        Epic epic1 = fileBackedTaskManager1.createEpic(new Epic("Epic with time", TaskStatus.NEW, "time epic"));
        Subtask subtask1 = fileBackedTaskManager1.createSubTask(new Subtask("Subtask with time", TaskStatus.NEW,
                "time subtask", LocalDateTime.of(2024, 4, 4, 15, 20),
                Duration.ofMinutes(20), 5));
        System.out.println(epic1);
        Subtask subtask2 = fileBackedTaskManager1.createSubTask(new Subtask("Subtask2 with time", TaskStatus.NEW,
                "time subtask2", LocalDateTime.of(2024, 4, 4, 20, 31),
                Duration.of(50, ChronoUnit.MINUTES), 5));
        System.out.println(epic1);
        fileBackedTaskManager1.updateSubTask(new Subtask(7, "Subtask2 with time", TaskStatus.IN_PROGRESS,
                "time subtask2", LocalDateTime.of(2024, 4, 4, 21, 55),
                Duration.of(50, ChronoUnit.MINUTES), 5));
        System.out.println(epic1);


        Task task2 = fileBackedTaskManager1.createTask(new Task("Task with time 22", TaskStatus.NEW, "time task 22",
                LocalDateTime.of(2024, 4, 3, 19, 30), Duration.ofMinutes(30)));
        Task task3 = fileBackedTaskManager1.createTask(new Task("Task not added to priority", TaskStatus.NEW, "time task3",
                LocalDateTime.of(2024, 4, 3, 18, 30), Duration.ofMinutes(70)));


        FileBackedTaskManager fileBackedTaskManager2;
        fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Проверка чтения из файла" + fileBackedTaskManager2.getSubTasks());
        System.out.println("Проверка чтения из файла" + fileBackedTaskManager2.getEpics());

        Task task4 = fileBackedTaskManager1.createTask(null);

    }
}
