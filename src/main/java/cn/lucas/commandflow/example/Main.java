package cn.lucas.commandflow.example;

import cn.lucas.commandflow.CommandFlow;
import cn.lucas.commandflow.CommandFlowBuilder;

/**
 * 示例主类
 */
public class Main {
    public static void main(String[] args) {
        // 创建自定义命令处理器
        SystemCommands systemCommands = new SystemCommands();
        CustomCommands customCommands = new CustomCommands();

        // 使用构建器创建CommandFlow实例
        CommandFlow commandFlow = new CommandFlowBuilder()
                .registerHandler(systemCommands)
                .registerHandler(customCommands)
                .setOutputHandler(message -> System.out.println("[CommandFlow] " + message))
                .autoStart(true)
                .build();

        // 或者直接使用以下方式
        // CommandFlow commandFlow = CommandFlow.getInstance();
        // commandFlow.registerHandler(systemCommands);
        // commandFlow.registerHandler(customCommands);
        // commandFlow.initConsoleInput();
    }
} 