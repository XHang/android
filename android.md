
# 1 Android Studio
不用多说，这是一个专门用来开发安卓app的程序
它在新建项目那里提供了一些模板辅助初始开发
## 1.1 模板
1. Empty Activity, 这个模板只有一个页面，展示``Hello Android!``
## 1.2 项目名
安卓的项目名允许空格，比如说`Greeting Card`

## 1.3 资源管理器
以防你不知道，这个IDE附带了一个资源管理器，你可以在这里到导入资源，比如说图片资源
仙人指路`View > Tool Windows > Resource Manager`

# 2 UI相关
## 2.1 注解
### 2.1.1 @Composable
如果函数上面有`@Composable`,说明这个函数是被Jetpack Compose使用专门用来生成UI的
特点
1. 这些函数可以在`setContent` 函数内使用，或者被其他Composable函数内使用
2. 这些函数名的首字母必须大写
3. 这些函数没有返回值
4. 每一个函数都应该有可选的一个类型是Modifier的参数，用来增强或者装饰Composable
### 2.1.2 @Preview
在这个函数上写这个注解，可以让Android Studio 在不需要启动App的情况下，预览这个函数生成的UI。
示例
```
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview1() {  
    GreetingCardTheme {  
        Greeting("Link")  
    }  
}
```
它接受一个参数showBackground，指定预览的UI要不要显示UI背景
这个单纯只是用来开发显示用，实际上运行时不需要这个

## 2.2 容器
在安卓开发里，容器代表UI的一部分
常用的容器有
### 2.2.1 Surface
这个容器可以修改背景和边距
> 它的命名参考了现实的`表面`，比如说桌子的表面，你可以在上面放置东西，桌子表面也可以有自己的颜色


使用实例
```
@Composable  
fun Greeting(name: String, modifier: Modifier = Modifier) {  
    Surface(color = Color.Red) {  
        Text(  
            text = "Hi, my name is $name!", modifier = modifier  
        )  
    }
```
里面的Surface包含了Text，这样就可以修改Text的背景颜色为红色，当然你也可以不需要任何容器，直接去掉Surface即可
### 2.2.2 装饰
#### 2.2.2.1 padding
这个装饰可以修改内边距，也就是容器内容和容器边框之间的距离。
看上去就好像内容和边框中间填充了一层，所以叫padding
使用示例
```
@Composable  
fun Greeting(name: String, modifier: Modifier = Modifier) {  
    Surface(color = Color.Red) {  
        Text(  
            text = "Hi, my name is $name!", modifier = modifier.padding(12.dp)  
        )  
    }  
}
```

### 2.2.3 位置
容器的位置通过参数来决定，有些容器带有参数比如说`verticalArrangement`
可以给它赋值，这样就可以决定该容器在UI的位置
示例
```
Column (modifier = modifier.padding(8.dp),verticalArrangement  =  Arrangement.Center,) {  
    Text(text = message,)  
    Text(text = from,)  
}
```
如上，Arrangement.Center参数决定了Column容器会放在UI中间
Text示例
```
Column (verticalArrangement  =  Arrangement.Center,) {  
    Text(text = message, textAlign = TextAlign.Center)  
    Text(text = from,
        modifier = modifier.padding(6.dp).  
        align(alignment = Alignment.End))  
}
```
`textAlign = TextAlign.Center` 和  `align(alignment = Alignment.End)` 决定了文字在容器内的布局


## 2.3 大小
安卓使用两种衡量单位
1. `dp`, 也就是[_density-independent pixels_ (DP)](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/package-summary#\(kotlin.Int\).dp\(\)),
2. `sp`, 也就是 [_scalable pixels_ (SP)](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/package-summary#\(kotlin.Float\).sp\(\)) ，这个单位的大小会随着手机设置的字体大小变化而变化
### 2.3.1 `sp`
代码示例
```
@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        fontSize = 100.sp,
        lineHeight = 116.sp,
    )
```

## 2.4 UI布局
UI布局基于`containment`,译为包含关系，意味着component里面可以包含一个或者多个`component`
被包含的在术语里被称为`Childred`
起到包含作用的那个`component`就被称为`parent`
在安卓开发中，有几类的作为`parent`的`component` 比较常见
1. Box
2. Column
3. Row：里面的UI元素以行方式呈现
上面的`component` 函数都接受`composable` 函数来作为它们的参数，这样子那些参数`composable`的布局就固定了


## 2.5 图片
### 2.5.1 导入
在安卓系统里，你可以在资源管理者里面导入图片资源，也可以导入Drawables
注意：由于安卓设备众多，同样的一块面积，有些设备含更多的像素，有些含更小的像素，为了避免图片显示异常，你必须在导入资产之前考虑`Density`
resize：不要轻易rezie图片，如果resize之后图片超出了安卓系统能处理的范围，系统立马就抛出一个内存溢出然后死给你看，所以，如果图片资产是用来作为背景或者照片显示使用，你应该用`drawable`来导入它们

### 2.5.2 composable





