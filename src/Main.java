import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.FileBackedTaskManager;

import java.io.File;

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


    }
}
