public class Event extends Task{
    protected String eventStart;
    protected String eventEnd;

    public Event(String task_detail, String eventStart, String eventEnd) {
        super(task_detail);
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    @Override
    public String toString(){
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)", eventStart, eventEnd);
    }
}
