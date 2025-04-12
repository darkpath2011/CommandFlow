package cn.lucas.commandflow.enums;

/**
 * 命令分类枚举
 */
public enum CommandCategory {
    SYSTEM("系统"),
    ADMIN("管理"),
    USER("用户"),
    TOOL("工具"),
    UTILITY("实用工具"),
    DEVELOPMENT("开发"),
    CUSTOM("自定义");

    private final String displayName;

    CommandCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 