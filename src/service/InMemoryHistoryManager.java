package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    LinkedList<Task> history = new LinkedList<>();

    @Override
    public  void add(Task task) {
        if (history.size() > 9) {
            history.removeFirst();
        }
        Epic savedEpic;
        Subtask savedSubtask;
        if (task instanceof Epic) {
           savedEpic = new Epic((Epic) task);
            history.add(savedEpic);
        } else if (task instanceof Subtask) {
            savedSubtask = new Subtask((Subtask) task);
            history.add(savedSubtask);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getAll() {
        return history;
    }
}
