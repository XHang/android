# 1 Android Studio

不用多说，这是一个专门用来开发安卓app的程序
它在新建项目那里提供了一些模板辅助初始开发

## 1.1 模板

1. Empty Activity, 这个模板只有一个页面，展示``Hello Android!``
   
   ## 1.2 项目名
   
   安卓的项目名允许空格，比如说`Greeting Card`

## 1.2 资源管理器

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

### 2.2.2 Box

Box容器内可以允许其他UI元素相互重叠，还可以调整里面的对齐方式

代码示例

```
Box(modifier = modifier) {
        Image(
            painter = image,
            contentDescription = null
        )
        GreetingText(message,from,modifier.fillMaxSize().padding(8.dp),)
    }
```

这么写的话，Box首先把图片放上去，然后是文字，因为文字是最后一行，所以最后的展示效果就是文字在图片上方。

### Spacer
这个容器可以交叉放在Column或者Row的作用域间隔，用来创建一个空白空间（其实用padding也可以做到）
```
Column(modifier = Modifier.padding(33.dp)) {  
    Contact(modifier = modifier,stringResource(R.string.phone))  
    Spacer(modifier = Modifier.height(6.dp))  
    Contact(modifier = modifier,stringResource(R.string.social_media))  
    Spacer(modifier = Modifier.height(6.dp))  
    Contact(modifier = modifier,stringResource(R.string.email))  
}
```

### 2.2.3 装饰

#### 2.2.3.1 padding

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

这里的12是把上下左右的内边距全部设置为12了，如果你想分别指定上下左右的内边据，可以这么搞

```
 Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )        
        )
```





### 2.2.4 布局

UI布局基于`containment`,译为包含关系，意味着component里面可以包含一个或者多个`component`被包含的在术语里被称为`Childred`起到包含作用的那个`component`就被称为`parent`在安卓开发中，有几类的作为`parent`的`component` 比较常见

1. Box
2. Column
3. Row：里面的UI元素以行方式呈现上面的`component` 函数都接受`composable` 函数来作为它们的参数，这样子那些参数`composable`的布局就固定了

要了解布局，首先了解什么是Alignment(对齐)和Arrangement（布局）

一般来说。Alignment的决定一个组件在容器内的位置，Arrangement则是多个组件情况下应该排列组合。

不过具体到容器里，Alignment管的是Children在主轴怎么排，而Arrangement管的是Children在交叉轴怎么排

而主轴和交叉轴要在具体的容器里，你才知道是哪个

#### 2.2.4.1 Column

##### 2.2.4.1.1 arrangement

对于Column来说，它的Children是竖直排的，所以竖直方向就是Column的主轴

代码示例

```
 Column(
        verticalArrangement = Arrangement.Top, // take a look at this!
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 100.sp,
            lineHeight = 116.sp,
        )
        Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier

        )
    }
```

这样子，Column那两个Text元素就会移动到Column容器的最顶端

它可以选择

1. verticalArrangement = Arrangement.Center  放在中间

2. verticalArrangement = Arrangement.Bottom 放在最低端

##### 2.2.4.1.2 alignment

一旦column决定了Children在垂直轴的位置，接下来的水平轴位置就要靠alignment来决定了。

比如都是最低端，有的可以靠左放，有的可以放中间，有的可以放右边。

代码示例

```
Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start, // look at here
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 36.sp,
            lineHeight = 116.sp,
            modifier = Modifier
        )
        Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier

        )
    }
```

这样Children的位置就是底部靠左放，你还可以选择

1. Alignment.CenterHorizontally 底部中间

2. horizontalAlignment = Alignment.End 底部靠右边
   
   

#### 2.2.4.2 Row

既然你已经知道了Column，那么Row就很简单了。

Arrangement管主轴，也就是横向怎么排

Alignment管交叉轴，也就是竖直怎么排

代码示例

```
 Row(
        horizontalArrangement = Arrangement.Start,    //水平方向的靠左边
        verticalAlignment = Alignment.CenterVertically, //竖直方向的中间位置 
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 10.sp,
            modifier = Modifier
        )
        Text(
            text = from,
            fontSize = 10.sp,
            modifier = Modifier

        )
    }
```

#### 2.2.4.3 box

box没有什么主轴交叉轴，决定box内children的布局就是它的一个参数contentAlignment

示例

```
    Box(
        contentAlignment = Alignment.BottomEnd, //children放置在最底下，然后靠右边
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 10.sp,
            modifier = Modifier
        )
        Text(
            text = from,
            fontSize = 10.sp,
            modifier = Modifier

        )
    }
```













容器内Children的布局通过参数来决定，有些容器带有参数比如说`verticalArrangement`
可以给它赋值，这样就可以决定Children在UI的位置，前提是容器比Children要大
示例

```
Column (modifier = modifier.padding(8.dp),verticalArrangement  =  Arrangement.Center,) {  
    Text(text = message,)  
    Text(text = from,)  
}
```

如上，Arrangement.Center参数决定了Column容器的所有Text会居中



### 2.2.5 对齐

对齐Align也算是跟位置有关，它调整的是Children在垂直方向的

Text示例

```
Column (verticalArrangement  =  Arrangement.Center,) {  
    Text(text = message, textAlign = TextAlign.Center)  
    Text(text = from,
        modifier = modifier.padding(6.dp).  
        align(alignment = Alignment.End))  
}
```

具体细分到某个Text,还可以继续指定它的位置，虽然Text大体都在居中，但你想当微操大师，居中再向右一点点，可以这么写

`textAlign = TextAlign.Center` 和  `align(alignment = Alignment.End)` 决定了文字的布局



## 2.3 大小

安卓使用两种衡量单位

1. `dp`, 也就是[_density-independent pixels_ (DP)](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/package-summary#\(kotlin.Int\).dp\(\)),

2. `sp`, 也就是 [_scalable pixels_ (SP)](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/package-summary#\(kotlin.Float\).sp\(\)) ，这个单位的大小会随着手机设置的字体大小变化而变化

### 2.3.1 sp

代码示例
    @Composable
    fun GreetingText(message: String, modifier: Modifier = Modifier) {
     Text(
         text = message,
         fontSize = 100.sp,
         lineHeight = 116.sp,
     )

### 2.3.2 weight

modifier有一个函数名叫Weight，它是一个拓展函数，一般只在Coumn和Row的作用域才起到作用

> 关于这个语法，请参考带接收者的lambda表达式

它执行后生成一个新配置好的modifier，可以把这个新的modifier传给该容器子元素composable

它在不同作用域起到的作用也不同，下面是两种它可以生效的作用域

#### 2.3.2.1 column

当你放置column后，你就可以在column的content（本质是一段lambda代码）内用modifier指定每个children分配的高度

伪代码代码示例

```
 Column(modifier = modifier) {
        Row(modifier = modifier.weight(1.0f,true)){
            Composable(
               ...
            )
            Composable(
                ...
            )
        }
        Row(modifier = modifier.weight(1.0f,true)) {
            Composable(
                ...
            )
            Composable(
               ...
            )
        }
    }
```

虽然看起来有点抽象，明明modifier.weight是作为传给Row，为什么反而调整的是高度分配，因为它是放在Column作用域内。

因为Column里面只有两个Row子容器，设置weight全都是1意味着Column里面的高度被两个Row平均分配

其中第二个参数是，子容器是否应该吃掉parent给它分配的所有空间，

1. true的话就是我踏马吃吃吃

2. false就是虽然你分配给我那么多空间，但是我只占用内容需要的空间

#### 2.3.2.2 Row

类似于Column，Row作用域内调节的就是子容器们分配的宽度了。

代码示例

```
Row(modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Composable(
                name = stringResource(R.string.text),
                description = stringResource(R.string.text_description),
                modifier = modifier.weight(1.0f, true),
                color = 0xFFEADDFF
            )
            Composable(
                name = stringResource(R.string.image),
                description = stringResource(R.string.text_description),
                modifier = modifier.weight(1.0f, true),
                color = 0xFFD0BCFF
            )
        }
```

这个Row里面有两个composable子容器，对应的weight都是1，也就是说parent给子容器分配的宽度都是均等的。

#### 2.3.2.3 fillMaxHeight

在告诉你这个玩意是啥之前，先说说之前经历写Quadrant的一个BUG.

这个程序最终展示的是一个四宫格，每一个四宫格上面居中排列文字。

我的分析是这样的

1. 从上到下看，可以把整个页面看作是一个Column，里面是两个Children，从上到下排列

2. 给两个Children分配weight为1，就是说，Column把它的所有空间，上下平均分配给的Children，形象点比较就是汉字`日`

3. 具体到每个Children，其实它们的类型是Row，形象点比较就是𫩏

4. 这些Children里面又存了Children（Column包含的文本），分别再设置它的weight为1，就是说Row给里面的Children分配了均等的宽度

好像这样就行了，但实际运行后发现最后的Column包含的文本并没占满整个父容器分配给它的高度，宽度倒是满足了

其实原因是row给children分配的是宽度，没指定分配高度，在row执行域里面，可以用modifier调用fillMaxHeight/fillMaxWidth来让那部分没指定的，全部占用掉





## 2.4 资源

### 2.4.1 导入

在安卓系统里，你可以在资源管理者里面导入图片资源，也可以导入Drawables
注意：由于安卓设备众多，同样的一块面积，有些设备含更多的像素，有些含更小的像素，为了避免图片显示异常，你必须在导入资产之前考虑`Density`
resize：不要轻易rezie图片，如果resize之后图片超出了安卓系统能处理的范围，系统立马就抛出一个内存溢出然后死给你看，所以，如果图片资产是用来作为背景或者照片显示使用，你应该用`drawable`来导入它们

### 2.4.2 引用

Android编译器会为你的资源自动生成一个R类文件，通过这个R类文件你就可以引用带具体的资源文件

比如说我想把图片加载进来

代码示例

```
val image = painterResource(R.drawable.androidparty)
```

其中

1. drawable就是res文件下面的子文件夹

2. androidparty就是对于的图片文件名 

### 2.4.3 图片

#### 2.4.3.1 应用

想在UI里面创建一个图片元素，需要用的函数是Image()

调用示例
    val image = painterResource(R.drawable.androidparty)
    Image(painter  = image, contentDescription = null)

其中第二个参数就是对这个图片的描述，到时无障碍功能阅读时，因为阅读无法把图片读出来，所以会用contentDescription来代替。

这里设置为Null，到时屏幕朗读到这里时，会跳过。

#### 2.4.3.2 缩放

首先来了解几个技术术语

1. scale： 缩放，原本意思是梯子->刻度->比例->动词化变成缩放

2. aspect ratio： 长宽比例

3. crop: 一般是等比缩放铺满屏幕后，多余的部分裁剪掉

在安卓提供的Image函数里面，就有一个contentScale参数用来指定图片的缩放方式

代码示例

```
 Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,

        )
```
但这个缩放设置不改变原有布局，只改变视觉大小。
所以如果设置的值超过了容器的大小，就会覆盖其他元素。
如果这不是你需要的，那么你得换另一个Size来调整图片的大小
#### 2.4.3.3 Size()
Similar to 缩放，它也可以用来调整图片的大小，但是布局也会随着改变。
这样改就不会占用到其他Composable的空间了
```
Icon(  
    painterResource(R.drawable.baseline_accessible_forward_24),  
    contentDescription = null,  
    modifier = Modifier.size(230.dp,230.dp)  
)
```
#### 2.4.3.4 透明度

在代码里，可以用alpha参数来指定图片的透明度

> 为什么叫alpha，其实是沿自一个经典的线性插值公式**结果颜色 = α × 前景色 + (1 - α) × 背景色**
> 
> 其中作者用了α来表示前景和背景混合的比例，被计算机用来指代透明度了

示例

```
  Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5f
        )
```

### 2.4.4 字符串

在代码里面写死字符串是坏习惯，所以建议把代码里的文字全部提取到一个文件里，放在`res/values/strings.xml`

这就是现在安卓开发推荐的代码标准。

在Android Studio那里，要做到这点也很简单。

选中代码中的文字，选择` **Extract String Resource**`

接下来给你的字符串命个名字，推荐是小写并且单词中间用下划线分开。

点击确定就完成了

原本代码可能是

```
GreetingImage(message = "Happy Birthday Sam!", from = "From Emma")
```

就会自动变成

```
GreetingImage(message =  stringResource(R.string.happy_birthday_text), from = stringResource(R.string.signature_text))
```

> 好像反而变长了...

同时会在res/value那里生成对应的字符串，大概酱紫

```
<resources>
    <string name="app_name">Birthday Greeting Card</string>
    <string name="happy_birthday_text">Happy Birthday Sam!</string>
    <string name="signature_text">From Emma</string>
</resources>
```
