gradle wrapper的配置文件


由来

在使用Android Studio作为IDE开发Android的应用的时候，我们可以直接使用IDE集成的build工具对应用进行编译打包，也可以使用命令行的形式执行gradle命令来做同样的事情，也就是基于gradle来构建android应用的build系统。
在需要用到gradle的时候，我们可以在我们的电脑上安装gradle，然后配置好环境变量后就可以使用了。
但是当我们把项目分享给电脑上没有gradle的人时，问题就来了。或者我们在一个没有装gradle的server上build的时候也会出现同样的问题。

所以基于此，gradle系统引入了我们今天的主角-gralde wrapper: 一个gradle的封装体。有了gradle wrapper，即便你的机器上没有安装gradle，也可以执行gradle的构建工作了。


Wrapper是对Gradle的一层包装,便于在团队开发过程中统一Gradle构建的版本。

Wrapper启动Gradle时会检查Gradle有没有被下载关联，若没有就会从配置的地址下载并运行构建。

|____gradle
| |____wrapper
| | |____gradle-wrapper.jar  //具体业务逻辑
| | |____gradle-wrapper.properties  //配置文件
|____gradlew  //Linux 下可执行脚本
|____gradlew.bat  //Windows 下可执行脚本

gradle-wrapper.properties
    — gradle-version	用于指定使用的Gradle版本
    — gradle-distribution-url	用于指定下载Gradle发行版的url地址

distributionBase=GRADLE_USER_HOME // 下载Gradle压缩包解压后存储的主目录
distributionPath=wrapper/dists // 相对于distributionBase的解压后的Gradle的路径
zipStoreBase=GRADLE_USER_HOME // 类似distributionBase，但存放压缩包
zipStorePath=wrapper/dists // 类似distributionPath，但存放压缩包
distributionUrl=https\://services.gradle.org/distributions/gradle-4.1-bin.zip // Gradle发行版压缩包的下载地址
