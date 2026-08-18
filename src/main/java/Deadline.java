public class Deadline extends Task{
    protected String deadline;
    public Deadline(String task_detail, String deadline) {
        super(task_detail);
        this.deadline =  deadline;
    }

    @Override
    public String toString(){
        return "[D]" + super.toString() + String.format(" (by: %s)", deadline);
    }
}
