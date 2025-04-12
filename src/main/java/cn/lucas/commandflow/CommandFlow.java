package cn.lucas.commandflow;

import cn.lucas.commandflow.annotation.Command;
import cn.lucas.commandflow.annotation.SubCommand;
import cn.lucas.commandflow.model.CommandContext;
import cn.lucas.commandflow.model.CommandInfo;
import cn.lucas.commandflow.model.CommandResult;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * CommandFlow核心类
 */
public class CommandFlow {
    /**
     * 单例模式
     */
    private static CommandFlow instance;
    private final Map<String, CommandInfo> commandMap = new ConcurrentHashMap<>();
    private final Map<String, CommandInfo> aliasMap = new ConcurrentHashMap<>();
    private Consumer<String> outputHandler = System.out::println;
    private Function<Object, Boolean> permissionChecker = o -> true;
    private boolean shouldContinue = true;
    private boolean initialized = false;

    private CommandFlow() {
        // 私有构造函数
    }

    public static CommandFlow getInstance() {
        if (instance == null) {
            synchronized (CommandFlow.class) {
                if (instance == null) {
                    instance = new CommandFlow();
                }
            }
        }
        return instance;
    }

    /**
     * 注册命令处理器
     *
     * @param handler 命令处理器对象
     */
    public void registerHandler(Object handler) {
        Class<?> handlerClass = handler.getClass();

        // 注册主命令
        for (Method method : handlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command commandAnnotation = method.getAnnotation(Command.class);
                registerCommand(commandAnnotation, handler, method);
            }
        }

        // 注册子命令
        for (Method method : handlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                SubCommand subCommandAnnotation = method.getAnnotation(SubCommand.class);
                registerSubCommand(subCommandAnnotation, handler, method);
            }
        }
    }

    /**
     * 注册命令
     */
    private void registerCommand(Command annotation, Object handler, Method method) {
        String commandName = annotation.name().toLowerCase();

        if (commandMap.containsKey(commandName)) {
            outputHandler.accept("警告: 命令 '" + commandName + "' 已被注册, 将被覆盖!");
        }

        CommandInfo commandInfo = new CommandInfo.Builder()
                .name(commandName)
                .description(annotation.description())
                .aliases(annotation.aliases())
                .permission(annotation.permission())
                .usage(annotation.usage())
                .category(annotation.category())
                .enabled(annotation.enabled())
                .handler(handler)
                .method(method)
                .build();

        commandMap.put(commandName, commandInfo);

        // 注册别名
        for (String alias : annotation.aliases()) {
            String aliasLower = alias.toLowerCase();
            aliasMap.put(aliasLower, commandInfo);
        }
    }

    /**
     * 注册子命令
     */
    private void registerSubCommand(SubCommand annotation, Object handler, Method method) {
        String parentName = annotation.parent().toLowerCase();
        CommandInfo parentCommand = commandMap.get(parentName);

        if (parentCommand == null) {
            parentCommand = aliasMap.get(parentName);
        }

        if (parentCommand == null) {
            outputHandler.accept("警告: 父命令 '" + parentName + "' 不存在, 子命令 '" + annotation.name() + "' 将被忽略!");
            return;
        }

        CommandInfo subCommandInfo = new CommandInfo.Builder()
                .name(annotation.name().toLowerCase())
                .description(annotation.description())
                .aliases(annotation.aliases())
                .usage(annotation.usage())
                .enabled(annotation.enabled())
                .handler(handler)
                .method(method)
                .build();

        parentCommand.addSubCommand(subCommandInfo);
    }

    /**
     * 设置输出处理器
     */
    public void setOutputHandler(Consumer<String> outputHandler) {
        this.outputHandler = outputHandler;
    }

    /**
     * 设置权限检查器
     */
    public void setPermissionChecker(Function<Object, Boolean> permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    /**
     * 执行命令
     */
    public CommandResult execute(String commandLine) {
        return execute(commandLine, null);
    }

    /**
     * 执行命令（带发送者）
     */
    public CommandResult execute(String commandLine, Object sender) {
        CommandContext context = new CommandContext(commandLine);
        context.setSender(sender);

        String commandName = context.getCommandName().toLowerCase();
        CommandInfo commandInfo = commandMap.get(commandName);

        if (commandInfo == null) {
            commandInfo = aliasMap.get(commandName);
        }

        if (commandInfo == null) {
            return CommandResult.error("未知命令: " + commandName);
        }

        if (!commandInfo.isEnabled()) {
            return CommandResult.error("命令已禁用: " + commandName);
        }

        // 权限检查
        if (!commandInfo.getPermission().isEmpty() && sender != null) {
            if (!permissionChecker.apply(sender)) {
                return CommandResult.permissionDenied("您没有执行此命令的权限");
            }
        }

        // 处理子命令
        String[] args = context.getArgs();
        if (args.length > 0 && !commandInfo.getSubCommands().isEmpty()) {
            String subCommandName = args[0].toLowerCase();
            CommandInfo subCommandInfo = commandInfo.findSubCommand(subCommandName);

            if (subCommandInfo != null) {
                // 移除第一个参数（子命令名）并创建新的上下文
                String newCommand = commandName + " " + String.join(" ",
                        Arrays.copyOfRange(args, 1, args.length));
                CommandContext subContext = new CommandContext(newCommand);
                subContext.setSender(sender);

                return executeMethod(subCommandInfo, subContext);
            }
        }

        return executeMethod(commandInfo, context);
    }

    /**
     * 执行命令方法
     */
    private CommandResult executeMethod(CommandInfo commandInfo, CommandContext context) {
        try {
            Method method = commandInfo.getMethod();
            method.setAccessible(true);

            Object result;
            Parameter[] parameters = method.getParameters();

            if (parameters.length == 0) {
                result = method.invoke(commandInfo.getHandler());
            } else if (parameters.length == 1 && parameters[0].getType().isAssignableFrom(CommandContext.class)) {
                result = method.invoke(commandInfo.getHandler(), context);
            } else if (parameters.length == 1 && parameters[0].getType().isAssignableFrom(String[].class)) {
                result = method.invoke(commandInfo.getHandler(), (Object) context.getArgs());
            } else {
                return CommandResult.error("命令方法参数不匹配");
            }

            if (result instanceof CommandResult) {
                return (CommandResult) result;
            } else if (result instanceof String) {
                return CommandResult.success((String) result);
            } else if (result == null) {
                return CommandResult.success();
            } else {
                return CommandResult.success("命令执行成功", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommandResult.error("命令执行错误: " + e.getMessage());
        }
    }

    /**
     * 初始化控制台输入
     */
    public void initConsoleInput() {
        if (initialized) {
            return;
        }

        initialized = true;
        Scanner scanner = new Scanner(System.in);

        while (shouldContinue) {
            String commandLine = scanner.nextLine().trim();

            CommandResult result = execute(commandLine);
            outputHandler.accept(result.toString());
        }

        scanner.close();
    }

    /**
     * 停止控制台输入
     */
    public void stop() {
        shouldContinue = false;
    }

    /**
     * 获取所有命令
     */
    public Collection<CommandInfo> getCommands() {
        return commandMap.values();
    }

    /**
     * 获取命令
     */
    public CommandInfo getCommand(String name) {
        CommandInfo command = commandMap.get(name.toLowerCase());
        if (command == null) {
            command = aliasMap.get(name.toLowerCase());
        }
        return command;
    }

    /**
     * 是否包含命令
     */
    public boolean hasCommand(String name) {
        return commandMap.containsKey(name.toLowerCase()) || aliasMap.containsKey(name.toLowerCase());
    }
} 