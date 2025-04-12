package cn.lucas.commandflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记子命令方法的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    /**
     * 子命令名称
     */
    String name();

    /**
     * 父命令名称
     */
    String parent();

    /**
     * 子命令描述
     */
    String description() default "";

    /**
     * 子命令别名
     */
    String[] aliases() default {};

    /**
     * 使用示例
     */
    String usage() default "";

    /**
     * 是否启用
     */
    boolean enabled() default true;
} 