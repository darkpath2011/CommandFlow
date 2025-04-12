package cn.lucas.commandflow.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 命令信息类
 */
public class CommandInfo {
    private final String name;
    private final String description;
    private final List<String> aliases;
    private final String permission;
    private final String usage;
    private final String category;
    private final boolean enabled;
    private final Object handler;
    private final Method method;
    private final List<CommandInfo> subCommands = new ArrayList<>();

    public CommandInfo(String name, String description, List<String> aliases,
                       String permission, String usage, String category,
                       boolean enabled, Object handler, Method method) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permission = permission;
        this.usage = usage;
        this.category = category;
        this.enabled = enabled;
        this.handler = handler;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public String getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Object getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

    public void addSubCommand(CommandInfo subCommand) {
        subCommands.add(subCommand);
    }

    public List<CommandInfo> getSubCommands() {
        return subCommands;
    }

    public CommandInfo findSubCommand(String name) {
        for (CommandInfo subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name) ||
                    subCommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(name))) {
                return subCommand;
            }
        }
        return null;
    }

    public static class Builder {
        private String name;
        private String description = "";
        private List<String> aliases = new ArrayList<>();
        private String permission = "";
        private String usage = "";
        private String category = "default";
        private boolean enabled = true;
        private Object handler;
        private Method method;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases = Arrays.asList(aliases);
            return this;
        }

        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder handler(Object handler) {
            this.handler = handler;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public CommandInfo build() {
            return new CommandInfo(name, description, aliases, permission,
                    usage, category, enabled, handler, method);
        }
    }
} 