package cn.lucas.commandflow.example;

import cn.lucas.commandflow.annotation.Command;
import cn.lucas.commandflow.annotation.SubCommand;
import cn.lucas.commandflow.model.CommandContext;
import cn.lucas.commandflow.model.CommandResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义命令示例
 */
public class CustomCommands {

    private final List<String> todoList = new ArrayList<>();

    @Command(
            name = "todo",
            description = "待办事项管理",
            aliases = {"task"},
            category = "UTILITY",
            usage = "todo <子命令> [参数]"
    )
    public CommandResult todo(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("请指定子命令，使用 'todo help' 查看帮助");
        }

        return CommandResult.success("使用子命令: todo " + context.getArgs()[0]);
    }

    @SubCommand(
            name = "add",
            parent = "todo",
            description = "添加待办事项",
            usage = "todo add <内容>"
    )
    public CommandResult todoAdd(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("用法: todo add <内容>");
        }

        String task = String.join(" ", context.getArgs());
        todoList.add(task);

        return CommandResult.success("已添加待办事项: " + task);
    }

    @SubCommand(
            name = "list",
            parent = "todo",
            description = "列出所有待办事项",
            aliases = {"ls"}
    )
    public CommandResult todoList() {
        if (todoList.isEmpty()) {
            return CommandResult.success("待办事项列表为空");
        }

        StringBuilder sb = new StringBuilder("待办事项列表:\n");
        for (int i = 0; i < todoList.size(); i++) {
            sb.append(i + 1).append(". ").append(todoList.get(i)).append("\n");
        }

        return CommandResult.success(sb.toString());
    }

    @SubCommand(
            name = "remove",
            parent = "todo",
            description = "删除待办事项",
            aliases = {"rm", "delete"},
            usage = "todo remove <编号>"
    )
    public CommandResult todoRemove(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("用法: todo remove <编号>");
        }

        try {
            int index = Integer.parseInt(context.getArgs()[0]) - 1;
            if (index < 0 || index >= todoList.size()) {
                return CommandResult.error("无效的待办事项编号");
            }

            String removed = todoList.remove(index);
            return CommandResult.success("已删除待办事项: " + removed);
        } catch (NumberFormatException e) {
            return CommandResult.syntaxError("编号必须是数字");
        }
    }
    
    @SubCommand(
            name = "clear",
            parent = "todo",
            description = "清空待办事项列表"
    )
    public CommandResult todoClear() {
        int count = todoList.size();
        todoList.clear();

        return CommandResult.success("已清空" + count + "个待办事项");
    }

    @SubCommand(
            name = "help",
            parent = "todo",
            description = "显示待办事项命令帮助"
    )
    public CommandResult todoHelp() {
        String sb = "待办事项命令帮助:\n" + "todo add <内容> - 添加待办事项\n" +
                "todo list - 列出所有待办事项\n" +
                "todo remove <编号> - 删除待办事项\n" +
                "todo clear - 清空待办事项列表\n";

        return CommandResult.success(sb);
    }

    @Command(
            name = "calc",
            description = "简单计算器",
            category = "UTILITY",
            usage = "calc <表达式>"
    )
    public CommandResult calc(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("用法: calc <表达式>");
        }

        String expression = String.join(" ", context.getArgs());
        try {
            // 这里仅做简单示例，实际应用中需要更复杂的表达式解析
            String[] parts = expression.split("\\s+");
            if (parts.length != 3) {
                return CommandResult.syntaxError("表达式格式错误，例如: 1 + 1");
            }

            double a = Double.parseDouble(parts[0]);
            String operator = parts[1];
            double b = Double.parseDouble(parts[2]);
            double result = 0;

            switch (operator) {
                case "+":
                    result = a + b;
                    break;
                case "-":
                    result = a - b;
                    break;
                case "*":
                    result = a * b;
                    break;
                case "/":
                    if (b == 0) {
                        return CommandResult.error("除数不能为零");
                    }
                    result = a / b;
                    break;
                default:
                    return CommandResult.syntaxError("不支持的操作符: " + operator);
            }

            return CommandResult.success(expression + " = " + result);
        } catch (NumberFormatException e) {
            return CommandResult.syntaxError("无效的数字格式");
        } catch (Exception e) {
            return CommandResult.error("计算错误: " + e.getMessage());
        }
    }
} 