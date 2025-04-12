# CommandFlow

一个开发者友好的Java命令管理器，旨在简化处理命令的过程，提高代码可维护性，并自动化重复性任务。

## 特点

- 基于注解的命令注册
- 支持主命令和子命令
- 灵活的命令执行流程
- 丰富的命令上下文
- 命令结果统一处理
- 权限系统集成
- 命令别名支持
- 命令分类管理
- 易于扩展和定制

## 快速开始

### 1. 创建命令处理器

```java
public class MyCommands {

    @Command(
            name = "hello",
            description = "打招呼命令",
            aliases = {"hi", "greet"},
            category = "USER"
    )
    public CommandResult hello(CommandContext context) {
        String name = context.getArg(0, "世界");
        return CommandResult.success("你好, " + name + "!");
    }

    @Command(
            name = "math",
            description = "数学计算命令",
            usage = "math <子命令> [参数]"
    )
    public CommandResult math(CommandContext context) {
        if (context.getArgs().length == 0) {
            return CommandResult.syntaxError("请指定子命令");
        }
        return CommandResult.success();
    }

    @SubCommand(
            name = "add",
            parent = "math",
            description = "加法计算",
            usage = "math add <数字1> <数字2>"
    )
    public CommandResult mathAdd(CommandContext context) {
        if (context.getArgs().length < 2) {
            return CommandResult.syntaxError("请提供两个数字");
        }

        int a = context.getArgAsInt(0, 0);
        int b = context.getArgAsInt(1, 0);
        int result = a + b;

        return CommandResult.success(a + " + " + b + " = " + result);
    }
}
```

### 2. 注册命令处理器并启动

```java
public class Main {
    public static void main(String[] args) {
        MyCommands myCommands = new MyCommands();

        // 使用构建器创建CommandFlow实例
        CommandFlow commandFlow = new CommandFlowBuilder()
                .registerHandler(myCommands)
                .setOutputHandler(System.out::println)
                .autoStart(true)
                .build();
    }
}
```

### 3. 执行命令

```java
// 直接执行命令
CommandResult result = commandFlow.execute("hello 张三");
System.out.

println(result.getMessage()); // 输出: 你好, 张三!

// 或者使用控制台输入
        commandFlow.

initConsoleInput();
```

## 高级用法

### 自定义权限检查

```java
commandFlow.setPermissionChecker(sender ->{
        // 检查发送者是否有权限
        if(sender instanceof User){
User user = (User) sender;
        return user.

hasPermission("admin");
    }
            return false;
            });
```

### 使用命令分类

```java

@Command(
        name = "user",
        description = "用户管理",
        category = "ADMIN",
        permission = "admin.user"
)
public CommandResult userCommand(CommandContext context) {
    // ...
}
```

### 处理命令结果

```java
CommandResult result = commandFlow.execute("math add 5 3");
if(result.

isSuccess()){
        System.out.

println("成功: "+result.getMessage());
        }else{
        System.err.

println("错误: "+result.getMessage());
        }
```

## 扩展

CommandFlow设计为易于扩展。您可以：

1. 创建自定义注解
2. 扩展命令上下文
3. 实现自己的命令处理逻辑
4. 添加更多命令结果类型

## 关于

CommandFlow是一个开源项目，欢迎贡献和反馈。 