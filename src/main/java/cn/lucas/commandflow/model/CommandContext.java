package cn.lucas.commandflow.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行上下文
 */
public class CommandContext {
    private final String rawCommand;
    private final String commandName;
    private final String[] args;
    private final Map<String, Object> attributes = new HashMap<>();
    private Object sender;
    private CommandResult result;

    public CommandContext(String rawCommand) {
        this.rawCommand = rawCommand.trim();
        String[] parts = this.rawCommand.split("\\s+", 2);
        this.commandName = parts[0];
        this.args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
        this.result = CommandResult.success();
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return index < args.length ? args[index] : null;
    }

    public String getArg(int index, String defaultValue) {
        return index < args.length ? args[index] : defaultValue;
    }

    public int getArgAsInt(int index, int defaultValue) {
        try {
            return Integer.parseInt(getArg(index));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getArgAsDouble(int index, double defaultValue) {
        try {
            return Double.parseDouble(getArg(index));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getArgAsBoolean(int index, boolean defaultValue) {
        String arg = getArg(index);
        if (arg == null) return defaultValue;
        return "true".equalsIgnoreCase(arg) || "yes".equalsIgnoreCase(arg) || "1".equals(arg);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public <T> T getAttribute(String key, Class<T> type) {
        Object value = attributes.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public CommandResult getResult() {
        return result;
    }

    public void setResult(CommandResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CommandContext{" +
                "commandName='" + commandName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
} 