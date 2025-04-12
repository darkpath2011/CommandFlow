package cn.lucas.commandflow.model;

/**
 * 命令执行结果
 */
public class CommandResult {
    private final Status status;
    private final String message;
    private final Object data;

    private CommandResult(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static CommandResult success() {
        return new CommandResult(Status.SUCCESS, "命令执行成功", null);
    }

    public static CommandResult success(String message) {
        return new CommandResult(Status.SUCCESS, message, null);
    }

    public static CommandResult success(String message, Object data) {
        return new CommandResult(Status.SUCCESS, message, data);
    }

    public static CommandResult error(String message) {
        return new CommandResult(Status.ERROR, message, null);
    }

    public static CommandResult permissionDenied(String message) {
        return new CommandResult(Status.PERMISSION_DENIED, message, null);
    }

    public static CommandResult syntaxError(String message) {
        return new CommandResult(Status.SYNTAX_ERROR, message, null);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Override
    public String toString() {
        return status + ": " + message;
    }

    public enum Status {
        SUCCESS, ERROR, PERMISSION_DENIED, SYNTAX_ERROR
    }
} 