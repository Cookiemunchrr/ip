import java.time.format.DateTimeParseException;
public class Parser {
    public Task parseEvent(TaskList taskList, String[] parts) throws MissingArgumentException, InvalidDateException, InvalidDurationException{
        try{
            String[] e = parts[1].split(" /from ", 2);
            String[] t = e[1].split(" /to ", 2);
            return new Event(e[0], t[0], t[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    public Task parseDeadline(TaskList taskList, String[] parts) throws MissingArgumentException, InvalidDateException{
        try{
            String[] d = parts[1].split(" /by ", 2);
            return new Deadline(d[0], d[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    public Task parseToDo(TaskList taskList, String[] parts) throws MissingArgumentException{
        try{
            if (parts[1].trim().isEmpty()){
                throw new MissingArgumentException(parts[0] + " <task>");
            }
            return new ToDo(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task>");
        }
    }

    public int parseTaskNumber(String[] parts) throws InvalidIndexException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            return index;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }


}
