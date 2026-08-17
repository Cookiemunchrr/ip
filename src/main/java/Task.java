public class Task {
    private boolean isDone = false;
    private String task_detail;

    public boolean checkTask(){
        return isDone;
    }

    public String view_task(){
        return task_detail;
    }

    public void mark(){
        isDone = true;
    }

    public void unmark(){
        isDone = false;
    }

    @Override
    public String toString(){
        if (isDone == true){
            return String.format("[X] %s", task_detail);
        } else{
            return String.format("[ ] %s", task_detail);
        }
    }

    public Task(String task_detail){
        this.task_detail = task_detail;
    }
}
