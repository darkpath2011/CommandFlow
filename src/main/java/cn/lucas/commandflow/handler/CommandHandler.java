package cn.lucas.commandflow.handler;

import cn.lucas.commandflow.model.CommandContext;
import cn.lucas.commandflow.model.CommandResult;

/**
 * 命令处理接口
 */
public interface CommandHandler {
    /**
     * 处理命令
     *
     * @param context 命令上下文
     * @return 命令执行结果
     */
    CommandResult handle(CommandContext context);
} 