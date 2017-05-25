# 美图秀秀

### 原理：反编译美图秀秀的apk文件，提取其中的so文件和JNI类，因为JNI类无法被混淆，因此可以直接使用。

### 问题
Android6.0适配问题，当程序运行在targetApi23以下的版本的时候，不会发生任何crash，当运行在23（Android6.0之上），会遇到crash闪退的情况。
### 解决方案：
Android 5.0 到 Android 6.0 + 的深坑之一 之 .so 动态库的适配
<http://www.cnblogs.com/linguanh/p/6137641.html?utm_source=tuicool&utm_medium=referral>

现在我用一句话说白它，就是：不同链接方式时，dlopen会打开指定的系统中(手机中)或提供的动态库，并使用 dlsym 获取符号地址，也就是说，如果，在此时的手机中如果找不到，那么就会出问题，一般和 API 有关系。

　　人为因素就是，编译这个 .so 库的人，他在编译的时候没考虑到下面这些情况，导致提供给别人用的时候，或者自己用的时候在高 API 版本手机出现问题。

　　感兴趣的就接着看下面详解吧！上面问题描述的第二点提到 .so 是运行在 Linux 环境下的，而且在 Android 里面一般由 NDK 编译，编译的时候，我们可以指明一种文件叫做 Application.mk，里面有一行 APP_STL := XXX 指明库的链接方式，默认是静态，STL的取值：

　　　　1）system，默认的值，最危险方式，直接和手机系统版本挂钩，采用手机最小版本的.so库链接

　　　　2）gabi++_static

　　　　3）gabi++_shared

　　　　4）stlport_static

　　　　5）stlport_shared

　　　　6）gnustl_static

　　　　7）gnustl_shared

　　如果不特别定义的话，“system”运行时库是默认的值。除此之外，凡是后面带“_static”的，表示其是一个静态链接的运行时库（运行时库的代码包含在编译后的程序中）；而凡是后面带“_shared”的，表示其是一个动态链接的运行时库（运行时库在程序运行时被动态加载进来）。如果去除动态或静态链接的因素，则除了默认的“system”运行时库之外，还有所谓的“gabi++”运行时库、“stlport”运行时库和“gunstl”运行时库。如果想支持C++异常的话，必须要使用gunstl运行时库。

　　主要是两种，静态链接，动态链接：

　　　　动态链接，是指在生成可执行文件时不将所有程序用到的函数链接到一个文件，因为有许多函数在操作系统带的dll文件中，当程序运行时直接从操作系统中找。

　　　　静态链接，是把所有用到的函数全部链接到 .so 文件中；

　　重点来了，上面说到了，静态链接是会把所需要的都搞到exe中，其实不然，这个说法是早期的了，对于现在的 Android 发展来说，为了使程序方便扩展，具备通用性，已经采用插件形式来链接动态库，编译时的静态和动态链接仅仅是程度问题。插件加载形式有：

　　　　1）dlopen

　　　　2）dlsym

　　　　3）dlclose

　　dlopen打开指定的系统中(手机中)动态库。并使用 dlsym 获取符号地址，也就是说，如果，在此时的手机中如果找不到，那么就会出问题，一般和 API 有关系。
