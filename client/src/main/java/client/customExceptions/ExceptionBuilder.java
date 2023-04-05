package client.customExceptions;

import commons.*;

import java.lang.reflect.Type;

public class ExceptionBuilder {

    private Type type;
    private String message;

    public ExceptionBuilder() {
        type = null;
        message = "";
    }

    public ExceptionBuilder type(final Type type){
        this.type = type;
        return this;
    }

    public ExceptionBuilder message(final String message){
        if(message != null)
            this.message = message;
        return this;
    }

    public Exception build(){
        if (type.equals(Board.class))
            return new BoardException(message);
        else if (type.equals(TaskList.class))
            return new TaskListException(message);
        else if (type.equals(Task.class))
            return new TaskException(message);
        else if (type.equals(SubTask.class))
            return new SubTaskException(message);
        else if (type.equals(Tag.class))
            return new TagException(message);
        return new Exception("Exception for " + type + ": " + message);
    }
}
