# android
# Android 布局
1. viewGroup  面板元素里面可以嵌套面板元素或者小组件元素
2. view  小组件元素，如按钮等
3. \app\src\main\res\layout\activity_main.xml是布局的xml文件


输入框的属性
1. hint 提示文字
2. Text 显示文本
3. Constraints 约束条件，就是控件所属位置的约束


## 设置按钮点击事件
假设你已经在布局上面添加了一个按钮了

1. 把点击事件的代码编写在MainActivity文件的类中

```
  /** Called when the user taps the Send button */
        public void sendMessage(View view) {
            // Do something in response to button
        }
​```java

```

2. 然后跳回布局文件（`activity_main`）

3. 点击按钮组件，理论上，会弹出一个属性（attributes）框,找到`onClick`那个设置，把刚刚写的方法设置进行

   不过，这个方法其实有几个需要注意的：

   1. 必须是public方法
   2. 必须接受`import android.view.View;`参数
   3. 返回值类型必须是void

# 基本知识

1. 每一个应用启动时只能访问自己所需的组件，而不能直接访问其他应用或者系统服务

   > 访问其他应用：可以使两个应用共享同一个用户id，为节省资源，还可以安排这两个应用运行在同一个jvm进程内
   >
   > 访问系统资源：需要经过用户允许

# 应用组件

一个app就是通过不同的组件构建而来的，组件之间互有依赖。

组件都有不同的用途和生命周期。

应用A可以启动应用B的某个组件C，这可以让这个组件C看起来好像就是这个应用A的东西

不过，启动时，会连带启动这个组件C的线程，而这个线程，属于应用 B

--启动组件--

通过`Intent` 来启动 Activity

组件大致分为以下几种

## Activity

简单的说，他就是显示在屏幕上面的界面，比如说，电子邮件上面的邮件列表，就是一个Activity

一个屏幕可以包含若干个Activity，从而给用户提供紧密的观看体验。

但实际上，Activity之前其实是独立的，如果应用程序许可，你甚至可以只显示其中一个Activity

比如说，拍照时顺手把照片作为附本发送个邮件，此时从拍照界面，就可以只弹出发送邮件的Activity

相关类：``Activity` `及其子类

## 服务

简单来说，服务就是后台长时间运行的一种程序，既然谈到后台了，那么可以肯定的是，它没有界面

广泛的说，服务有两种，一种是可有可无，一种是需要不停的刷存在感。

前者举个栗子：把国内某app打开，然后退出。去看任务管理器中，这个app的进程还存在，就是因为这个app的服务还在后台运行，可能会同步一些数据之类的，没有退出。像这种服务，如果RAM不足的话，就会被操作系统秒杀掉

后者举个栗子：后台播放音乐，像这种服务，操作系统要尽量保证它活着。它要死的话，用户就不爽了

当然服务与服务之间也存在依赖，比如后台播放音乐服务依赖服务B，那么，操作系统也要尽量保证服务B存活

相关类：[Service](https://developer.android.google.cn/reference/android/app/Service)及其子类



## 广播接收器

接受来自系统的通知（应用不需要活也可以通知），或者发通知给系统。就酱

## 内容提供程序

管理数据的，数据可以存放在各种各样的介质。随你便。

然后其他应用程序可以通过这个内容通过程序来读取数据或者写入数据。前提是内容提供程序允许









# 清单文件（AndroidManifest.xml）

应用程序启动时，会读取这个文件确认所有组件都存在。因此，你的应用涉及到哪些组件都要一五一十的写到这个文件里面去，并且放在根目录下。

另外，这个清单文件还有其他功能

1. 确定应用涉及到的权限
2. 根据应用使用的API，确定应用最低的API等级
3. 确定应用使用的软硬件功能
4. 声明应用需要链接的API库（Google地图库）

清单文件内容举例

```
<?xml version="1.0" encoding="utf-8"?>
<manifest ... >
    <application android:icon="@drawable/app_icon.png" ... >
        <activity android:name="com.example.project.ExampleActivity"
                  android:label="@string/example_label" ... >
        </activity>
        ...
    </application>
</manifest>
```

1. `android:icon`是应用的图标资源位置
2. `android:name` activity子类的完全限定名

## 声明权限

一个app需要什么权限需要在`AndroidManifest.xml`声明

声明的方式为

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.myapplication">
     <!--需要发送短信权限-->
    <uses-permission android:name="android.permission.SEND_SMS"/>
    <application>
    .....
    </application>
</manifest>
```

列举几个常用的权限

1. 允许应用访问外部存储

`<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />`

2. 允许应用写入外部存储

   ```
   <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="18" />
   ```

   > android:maxSdkVersion="18"表示 API 级别 18 及以前才需要声明这个权限
   >
   > API 级别 19写入外部存储不再需要声明这个权限了

2. 在SD卡中创建与删除文件权限

   ```
   <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
   ```

3. 允许挂载和卸载可移动存储的文件系统

   ```
   <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"
       tools:ignore="ProtectedPermissions" />
   ```



# Handler消息传递机制

Andriod为了线程安全，只有UI线程才能操作UI变更。其他自己创建的子线程不能更新UI

那么问题来了，如果是播放音乐程序子线程计算出音乐播放进度，要更新UI上面的进度条，为之奈何？

这个时候，就要引入Handler，子线程通过Handler发送消息，UI线程不断循环消息队列取消息来处理消息

（或者runOnUiThread()）

> UI线程：单app启动时，程序会创建UI线程（主线程）,用来负责对UI的处理，该线程同时也会创建Looper对象，以及与之关联的MessageQueue对象
>
> **Handler**： 发送和处理消息，通过`new`方法，实现其抽象函数而创建
>
> **Message**:Handler接收与处理的消息对象
>
> **MessageQueue**:消息队列,先进先出管理Message,在初始化Looper对象时会创建一个与之关联的MessageQueue;
>
> **Looper**:每个线程只能够有一个Looper,管理MessageQueue,不断地从中取出Message分发给对应的Handler处理



> **Handler** 主线程创建
>
> **Looper**  子线程创建





# 几个小问题

IDE 突然在某个代码上报`Call requires API level 16 (current min is 15):`

请在app文件夹里面把`build.gradle`里面的minSdkVersion修改成推荐的值，比如说16

