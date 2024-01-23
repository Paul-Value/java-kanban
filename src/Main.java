import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        // Тесты по Task
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask(new Task("Делать проект 4 спринта", "Понять как сделать проект 4 спринта!!!"));
        System.out.println("Создали задачу: " + task1);
        System.out.println("______________________________________");

        Task taskFromManager = taskManager.getTaskById(task1.getId());
        System.out.println("Получаем задачу по id: " + taskFromManager);
        System.out.println("______________________________________");

        taskManager.updateTask(new Task("В слезах делать проект", "Пересмотреть вебинар"), task1.getId(), TaskStatus.IN_PROGRESS);
        System.out.println("Обновили задачу: " + taskManager.getTaskById(1));
        System.out.println("______________________________________");

        Task task2 = taskManager.createTask(new Task("Сделать зарядку", "Потренироваться с утра для бодрости духа"));
        System.out.println("Создали задачу: " + task2);
        System.out.println("______________________________________");

        System.out.println("Получаем все задачи: " + taskManager.getTasks());
        System.out.println("______________________________________");

        taskManager.deleteTaskById(taskFromManager.getId());
        System.out.println("Удалили задачу по id: " + taskManager.getTaskById(1));
        System.out.println("Получаем все задачи: " + taskManager.getTasks());
        System.out.println("______________________________________");

        taskManager.deleteAllTasks();
        System.out.println("Удалили все задачи");
        System.out.println("Получаем все задачи: " + taskManager.getTasks());
        System.out.println("______________________________________");

        //Тесты по Epic
        Epic epic1 = taskManager.createEpic(new Epic("Выполнить проект 4 спринта",
                "Разбить на подзадачи "));
        System.out.println("Создали Эпик: " + epic1);
        System.out.println("______________________________________");

        Epic epicFromManager = taskManager.getEpicById(epic1.getId());
        System.out.println("Получаем Эпик по id: " + epicFromManager);
        System.out.println("______________________________________");

        taskManager.updateEpic(new Epic("Точно выполнить проект 4 спринта", "Посмотреть пачку для подбора информации"), 3);
        epic1 = taskManager.getEpicById(epic1.getId());
        System.out.println("Обновили Эпик: " + epic1);
        System.out.println("______________________________________");

        Epic epic2 = taskManager.createEpic(new Epic("Сделать рабочий проект", "Проект по созданию космического корабля"));
        System.out.println("Создали Эпик: " + epic2);
        System.out.println("______________________________________");

        System.out.println("Получаем все Эпики: " + taskManager.getEpics());
        System.out.println("______________________________________");

        System.out.println("Получаем все Сабтаски у Эпика: " + taskManager.getSubtasksFromEpic(epic1));
        System.out.println("______________________________________");

        taskManager.deleteEpicById(epicFromManager.getId());
        System.out.println("Удалили Эпик по id: " + epic1);
        System.out.println("Получаем все Эпики: " + taskManager.getEpics());
        System.out.println("______________________________________");

        taskManager.deleteAllEpics();
        System.out.println("Удалили все Эпики");
        System.out.println("Получаем все Эпики: " + taskManager.getEpics());
        System.out.println("______________________________________");

        //Тесты по SubTask
        Epic epic3 = taskManager.createEpic(new Epic("Проект 4 спринта",
                "Разбить на подзадачи "));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("Создать класс Task",
                "Создать родительский класс ", epic3));
        System.out.println("Создали Сабтаск у Эпика 3: " + epic3);
        System.out.println("Выводим отдельно подзадачу: " + subtask1);
        System.out.println("______________________________________");

        Subtask subtask2 = taskManager.createSubTask(new Subtask("Создать класс Epic",
                "Создать класс Epic наследника класса Task", epic3));
        System.out.println("Создали Сабтаск у Эпика 3: " + epic3);
        System.out.println("Выводим отдельно подзадачу: " + subtask2);
        System.out.println("______________________________________");

        Subtask subTaskFromManager = taskManager.getSubTaskById(subtask2.getId());
        System.out.println("Получаем Сабтаск по id: " + subTaskFromManager);
        System.out.println("______________________________________");

        Subtask updatedSubtask = new Subtask("Написать методы класса Epic", TaskStatus.IN_PROGRESS, "Подумать над реализацией класса", epic3);
        taskManager.updateSubTask(subtask2.getId(), updatedSubtask);
        System.out.println("Обновили статус Эпик: " + epic3);
        System.out.println("Обновили Сабтаск у Эпика 3: " + epic3.getSubTasks());
        System.out.println("Выводим отдельно подзадачу: " + taskManager.getSubTasks().get(subtask2.getId()));
        System.out.println("______________________________________");

        taskManager.deleteSubTaskById(subtask2.getId());
        System.out.println("Удалили Сабтаск по id: " + epic3);
        System.out.println("______________________________________");

        taskManager.deleteAllSubTasks();
        System.out.println("Удалили все Сабтаски");
        System.out.println("Получаем все Сабтаски: " + taskManager.getSubTasks());
        System.out.println("Получаем все Сабтаски у Эпика 3: " + epic3);
        System.out.println("______________________________________");
    }
}
