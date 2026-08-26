package quu.task;

import quu.exception.MissingArgumentException;

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

    public static ToDo fromFileString(String[] fields) throws MissingArgumentException{
        try{
            if (fields[2].trim().isEmpty()){
                throw new MissingArgumentException(fields[0] + " <task>");
            }
            ToDo todo = new ToDo(fields[2]);
            return todo;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(fields[0] + " <task>");
        }
    }
}
