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