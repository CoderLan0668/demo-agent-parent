# 这是一个什么项目

这是一个基于bytebuddy的java agent演示项目，这个项目本身并没有什么用， 但由于项目剥离了业务逻辑，
保留了java agent的骨架代码，可以在这个项目的基础上加深对java agent、bytebuddy的技术细节的掌握。

项目结构：

- demo-agent：java agent的主模块
- demo-agent-sdk：demo-agent提供给agent 插件开发的sdk
- demo-agent-plugins：在这个模块下放置demo-agent的插件
- demo-app：一个简单的示例应用，用于验证agent代码
- bytecode：用于研究字节码的模块

# how to run

```
mvn clean install -DskipTests=true
```
这一步主要完成几件事：

- plugins下的插件（目前就1个）会被打包成jar，然后被拷贝到项目根目录下的`plugins`目录
- demo-agent会被打包成jar

接下来需要让demo-app带着demo-gent跑起来，需要在demo-app的启动配置中加上参数
```

-javaagent:${pathToProject}/demo-agent-parent/demo-agent/target/demo-agent-1.0-SNAPSHOT.jar

```
还需要加上环境变量

```

PLUGINS_DIR=${pathToProject}/demo-agent-parent/plugins

```

然后就可以把demo-app跑起来了，可以按照自己的想法去修改代码，验证你的问题，探索java agent

注：建议idea上运行，因为目前agent的代码和app的代码是放在一起，并且agent打包的时候没有对bytebuddy
这些包shaded处理 因此很容易进行断点调试。

# 大纲

- 认识ClassLoader
  - 两个核心功能
  - 双亲委派，目的以及缺陷
  - java agent用什么类加载器加载？
- java agent机制
  - ClassFileTransformer
  - retransform/redefine
- byte buddy agent
  - type matcher
    - Ignored
  - Transformer.ForAdvice
    - Advice.WithCustomMapping
      - PostProcessor
      - OffsetMapping
      - invoke dynamic bsm
    - advice class name
    - ClassFileLocator
- agent的基本构成
  - agent.jar
  - plugin.jar
  - app.jar
- 双刃剑
  - 资源占用
    - 性能开销
    - 内存开销
  - 调试
    - arthas
    - 日志
    