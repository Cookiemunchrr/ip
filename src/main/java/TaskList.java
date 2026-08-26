import java.util.ArrayList;
import java.util.List;
public class TaskList {
    private final List<Task> todoList;

    public TaskList(){
        todoList = new ArrayList<>();
    }

    public TaskList(List<Task> todoList){
        this.todoList = todoList;
    }

    public List<Task> getTodoList(){
        return todoList;
    }

    public Task getItemAtIndex(int index){
        return todoList.get(index);
    }

    public int getSize(){
        return todoList.size();
    }

    public void add_to_list(Task task){
        todoList.add(task);
    }

    public Task remove_from_list(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.remove(index - 1);
        return task;
    }

    public Task mark_items(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.mark();
        return task;
    }

    public Task unmark_items(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.unmark();
        return task;
    }
}
