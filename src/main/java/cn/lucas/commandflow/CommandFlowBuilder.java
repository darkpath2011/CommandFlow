package cn.lucas.commandflow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * CommandFlow构建器
 */
public class CommandFlowBuilder {
    private final CommandFlow commandFlow;
    private final List<Object> handlers = new ArrayList<>();
    private Consumer<String> outputHandler = System.out::println;
    private Function<Object, Boolean> permissionChecker = o -> true;
    private boolean autoStart = false;

    public CommandFlowBuilder() {
        this.commandFlow = CommandFlow.getInstance();
    }

    /**
     * 注册命令处理器
     */
    public CommandFlowBuilder registerHandler(Object handler) {
        handlers.add(handler);
        return this;
    }

    /**
     * 设置输出处理器
     */
    public CommandFlowBuilder setOutputHandler(Consumer<String> outputHandler) {
        this.outputHandler = outputHandler;
        return this;
    }

    /**
     * 设置权限检查器
     */
    public CommandFlowBuilder setPermissionChecker(Function<Object, Boolean> permissionChecker) {
        this.permissionChecker = permissionChecker;
        return this;
    }

    /**
     * 设置自动启动
     */
    public CommandFlowBuilder autoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    /**
     * 构建CommandFlow
     */
    public CommandFlow build() {
        commandFlow.setOutputHandler(outputHandler);
        commandFlow.setPermissionChecker(permissionChecker);

        for (Object handler : handlers) {
            commandFlow.registerHandler(handler);
        }

        if (autoStart) {
            new Thread(commandFlow::initConsoleInput).start();
        }

        return commandFlow;
    }
} 