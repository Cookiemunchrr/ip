public class ToDo extends Task {
    public ToDo(String task_detail) {
        super(task_detail);
    }

    @Override
    public String toString(){
        return "[T]" + super.toString();
    }

    @Override
    public String toFileString(){
        return "T " + super.toFileString();
    }
}
