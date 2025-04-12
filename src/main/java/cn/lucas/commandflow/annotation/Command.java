package cn.lucas.commandflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记命令方法的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * 命令名称
     */
    String name();

    /**
     * 命令描述
     */
    String description() default "";

    /**
     * 命令别名
     */
    String[] aliases() default {};

    /**
     * 命令权限
     */
    String permission() default "";

    /**
     * 使用示例
     */
    String usage() default "";

    /**
     * 命令分类
     */
    String category() default "default";

    /**
     * 是否启用
     */
    boolean enabled() default true;
} 