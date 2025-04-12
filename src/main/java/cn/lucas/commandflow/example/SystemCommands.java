package cn.lucas.commandflow.example;

import cn.lucas.commandflow.annotation.Command;
import cn.lucas.commandflow.annotation.SubCommand;
import cn.lucas.commandflow.model.CommandContext;
import cn.lucas.commandflow.model.CommandResult;

/**
 * 系统命令示例
 */
public class SystemCommands {

    @Command(
            name = "help",
            description = "显示所有可用的命令",
            aliases = {"?", "commands"},
            category = "SYSTEM",
            usage = "help [命令名]"
    )
    public CommandResult help(CommandContext context) {
        // 实际实现会从CommandFlow获取所有命令信息
        String sb = "可用命令:\n" + "help - 显示帮助信息\n" +
                "version - 显示版本信息\n" +
                "echo - 回显消息\n" +
                "system - 系统相关命令\n";

        return CommandResult.success(sb);
    }

    @Command(
            name = "version",
            description = "显示系统版本信息",
            aliases = {"ver", "v"},
            category = "SYSTEM"
    )
    public CommandResult version() {
        return CommandResult.success("CommandFlow 版本 1.0.0");
    }

    @Command(
            name = "echo",
            description = "回显输入的文本",
            usage = "echo <文本>"
    )
    public CommandResult echo(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("用法: echo <文本>");
        }

        String message = String.join(" ", context.getArgs());
        return CommandResult.success(message);
    }

    @Command(
            name = "system",
            description = "系统相关命令",
            aliases = {"sys"},
            category = "SYSTEM",
            usage = "system <子命令> [参数]"
    )
    public CommandResult system(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("请指定子命令，使用 'system help' 查看帮助");
        }

        return CommandResult.success("使用子命令: system " + context.getArgs()[0]);
    }

    @SubCommand(
            name = "info",
            parent = "system",
            description = "显示系统信息",
            aliases = {"i"}
    )
    public CommandResult systemInfo() {
        String sb = "系统信息:\n" + "操作系统: " + System.getProperty("os.name") + "\n" +
                "系统版本: " + System.getProperty("os.version") + "\n" +
                "Java版本: " + System.getProperty("java.version") + "\n" +
                "内存使用: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB / " +
                Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB";

        return CommandResult.success(sb);
    }

    @SubCommand(
            name = "memory",
            parent = "system",
            description = "显示内存使用情况",
            aliases = {"mem"}
    )
    public CommandResult systemMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;

        String sb = "内存使用情况:\n" + "已用内存: " + usedMemory + "MB\n" +
                "空闲内存: " + freeMemory + "MB\n" +
                "总内存: " + totalMemory + "MB\n" +
                "最大内存: " + maxMemory + "MB\n";

        return CommandResult.success(sb);
    }

    @SubCommand(
            name = "help",
            parent = "system",
            description = "显示系统命令帮助",
            aliases = {"?"}
    )
    public CommandResult systemHelp() {
        String sb = "系统命令帮助:\n" + "system info - 显示系统信息\n" +
                "system memory - 显示内存使用情况\n";

        return CommandResult.success(sb);
    }
} 