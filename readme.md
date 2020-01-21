# 结论

    结论1: build.gradle 项目全局的gradle构建脚本 
    结论2: gradle.properties 全局的gradle配置文件，这里的配置属性会影响到项目中所有gradle编译脚本 
    结论3: gradlew和gradlew.bat 用于在命令行中执行gradle命令，前者在Linux/Mac使用，后者是Windows 
    结论4: gradlew和gradlew.bat 用于在命令行中执行gradle命令，前者在Linux/Mac使用，后者是Windows 
    结论5: MyApplication.iml iml文件是所有IntelliJ IDEA项目都会自动生成的一个文件（Androidstudio基于IntelliJ IDEA开发），无需修改此文件任何内容 
    结论6: local.properties 用于指定AndroidSDK的路径 
    结论7: settings.gradle 这个文件用于指定项目中所有引入的模块。由于HelloWorld项目中就只有一个app模块，因此该文件中也就只引入了app这一个模块。通常情况下模块的引入都是自动完成的，需要我们手动去修改这个文件的场景可能比较少。

# 发包
    1. 去掉 networkSecurityConfig（在 AndroidManifest.xml
    2. build 菜单
    3. 点击 Generate Signed APK
    4. 选择密匙 C:\Users\jiejay\rejiejay.jks
    5. 密码：DFqew1938167 别名: rejiejay

# 环境
    参考文档: 《BuildConfig与build.gradle的关系》https://www.jianshu.com/p/3d9b23afe514 
    原理: 
        1. build.gradle 文件 可以区分 release(正式环境 和 debug(开发环境 执行
        2. 代码可以根据运行方式不同，获取到build.gradle 文件下定义的字符串
    本地环境注意: 
        1. 运行Debug模式
        2. AndroidManifest.xml 关闭
