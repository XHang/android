# Kotlin语法

# 关键字
## 变量相关
- val 意味变量一旦赋值，不可再变（取自英语value，意味着它是一个值，不是变量）
- var 普通的变量声明


# 函数
语法
`func name(a:Int,b:Int)=a+b`
函数是可以写成一个表达式的，你甚至不需要写返回值，编译器自己推断

## 扩展函数
想为第三方库提供的类写一个自己的函数，但是你又不能修改到第三方库的源代码。
是时候看看Extended functions了。
比如说看看下面这个
```
fun String.bold():String = "<b>$this</b>"  
fun main(){  
    println("mine is bigger!".bold())  
}
```
上面的代码给String添加了扩展函数bold，调用它时就跟调用其他方法没什么两样。
在某些情况下还是挺有用的，比如说第三方类库只提供一个大而全的函数，但实际你经常使用的也就那几个参数，那么你就可以写一个拓展函数来方便调用了。



## 参数默认值
语法
```
func name(a:Int=2,b:Int)=a+b
```
在调用时，可以只传b的值
```
name(b=2)
```
这里由于默认值参数排在一般参数的前面，在调用时给第二个参数传值时，需要指定是哪个参数的值
## 范围函数
范围函数可以让你创建一个范围，变量只能在这个范围内使用，这样就避免变量的作用域太大从而造成不必要的麻烦，同时共享同一个命名空间（scope）
常用的范围函数有下面几种
### let
参考非空验证那一章
### apply
可以在新建对象后立即调用apply完成对象的一些初始化操作，比如
```
class Client() {  
    var token: String? = null  
    fun connect() = println("connected!")  
    fun authenticate() = println("authenticated!")  
    fun getData(): String {  
        println("getting data!")  
        return "Mock data"  
    }  
}  
  
fun main() {  
    val client = Client().apply {  
        token = "asf"  
        connect()  
        authenticate()  
    }  
    println(client.getData())  
}
```
这样代码更好读，组织性更好
### run
和apply类似，你也可以在run代码块里面为对象进行一些初始化操作，但是run允许你立刻用对象进行一些计算然后返回计算的结果。
例如
```
fun main() {  
    val result = Client().run {  
        token = "asf"  
        connect()  
        authenticate()  
        getData()  
    }  
  }
```

比如说你创建client只是为了最后调用getData拿到结果
### also
also函数方便在一个函数执行后，再执行一次额外的操作。
经常用于记录某次函数执行后的日志，或者链式操作，debug等等。
```
fun main() {
    val medals: List<String> = listOf("Gold", "Silver", "Bronze")
    val reversedLongUppercaseMedals: List<String> =
        medals
            .map { it.uppercase() }
            .also { println(it) }
            // [GOLD, SILVER, BRONZE]
            .filter { it.length > 4 }
            .also { println(it) }
            // [SILVER, BRONZE]
            .reversed()
    println(reversedLongUppercaseMedals)
    // [BRONZE, SILVER]
}
```
### with
当你想为某个对象执行一系列的操作，你总是得写`对象.成员方法`对吧。
用with范围函数的话，你可以把`对象`当作参数传进去，在这个范围函数里面执行的对象的任何方法，都不在需要在前面写对象了
```
class Canvas {
    fun rect(x: Int, y: Int, w: Int, h: Int): Unit = println("$x, $y, $w, $h")
    fun circ(x: Int, y: Int, rad: Int): Unit = println("$x, $y, $rad")
    fun text(x: Int, y: Int, str: String): Unit = println("$x, $y, $str")
}
val mainMonitorSecondaryBufferBackedCanvas = Canvas()
with(mainMonitorSecondaryBufferBackedCanvas) {
    text(10, 10, "Foo")
    rect(20, 30, 100, 50)
    circ(40, 60, 25)
    text(15, 45, "Hello")
    rect(70, 80, 150, 100)
    circ(90, 110, 40)
    text(35, 55, "World")
    rect(120, 140, 200, 75)
    circ(160, 180, 55)
    text(50, 70, "Kotlin")
}
```

## 常用函数
### takeIf
这个函数是成员函数，接受一个lambda，如果表达式的结果是true，则返回这个成员变量，否则返回Null


# 模板
Kotlin支持在字符串里面直接运用表达式，到时输出的字符串就会携带这个表达式的结构输出
想要在字符串里面表达这是一个表达式，则在表达式前面用`$` 标记
Example
```
val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"  
fun getPattern(): String = """\d{2}\s$month\s\d{4}"""
```
如果是访问对象的成员变量的话，可以这么写
```
println("the name of a contact is ${a.name}")
```

# 特色
## 非空验证(?)
一般声明的变量都是非空的，意味着你不能把值赋值为NULL.
除非你声明这个变量可以为空，语法为
```
var a:String? =NULL
```
对于可为空的变量，调用它的任何方法，必须检查它是不是为空，否则编译器不会通过的
对于这个检查，有几种方式
1. if 条件下
2. 安全调用符`?` ,例如`println(a?.length)` 如果a本来就是null，那么`a?.length`就返回null，或者`val l=a?.length?:0` 如果a不是空，那么表达式就是a.lentgh,否则就是0
### 我保证它是非空的(!!)
当你确信某个可空变量实际一定不为null时，又不想写安全调用符（你确信它非空），那么可以使用`!!` 运算符
Example
```
val a:String?="Kotlin"
val l=a!!.length
```

### 空接受者
某些对象的函数在调用时，这个对象是可以允许为空的，这种情况，就是接受者是空的。
你不需要强制先非空验证再调这个函数
Example
```
val person:Persion?=null
println(person.toString())

data class persion(val name:String)
```
如以上代码，toString返回的结构会是“null”这个字符串

### let方法
把`？`和`let`方法可以让程序先判断对象是不是为空，不是再去执行对应的代码
Example
```
val listWithNull:List<string?> = listOf("Kotlin",null)
for (item in listWithNull){
	item?.let{println(it)}
}
```



### 集合的filterNotNull
如果你有一个集合里面元素可能是null，而你又想排除里面为null的元素，那么你可以调用filterNotNull
Example

```
val nullableList:List<Int?> = listOf(1,2,null)
val intList List<int> = nullableList.filterNotNull() // 预期intList里面只有1，2
```
###  Elvisc操作符-指定默认值
使用`?`进行非空校验，默认如果真的是空，会返回Null,如果想指定其他不是Null的默认值，可以用Elvisc操作符`?:`
```
val str:String?=null  
println(str?.length?:0)
```
###  Elvisc操作符-空情况执行返回
如果遇到空的情况，除了上面指定默认值，还可以写return语句来提前返回
```
data class User(
    val id: Int,
    val name: String,
    // List of friend user IDs
    val friends: List<Int>
)

// Function to get the number of friends for a user
fun getNumberOfFriends(users: Map<Int, User>, userId: Int): Int {
    // Retrieves the user or return -1 if not found
    val user = users[userId] ?: return -1  // 如果用户id查不到，直接返回-1终止函数
    // Returns the number of friends
    return user.friends.size
}
```



## 安全类型转化
在Kotlin里，类型转换的关键字是`as`，但是如果目标不是指定类型，它会抛除一个异常。
为了安全转化，你可以使用`？`来安全转换
Example
```
val a:Any="Hello,Kotlin"
var aInt:Int?= a as? Int  //aInt预期会是null
var aString:String?= a as? String // aString预期会是字符串
```

## Nothing 类型
在Kotlin里面，所有表达式都有类型，`throw IllegalArgumentException()`的表达式类型就是Nothing。
Nothing是所有类型的子集，它可以用在泛型或者返回值上。
> 用在泛型的话，可以理解任意类型都可以接受Nothing（Any则是反过来）
> 就好像哺乳动物类下面是猫科动物。哺乳动物可以接受猫科动物

当它被出现在函数上时，通常表示这个函数永远无法正常完成，可能是永远抛出异常或者陷入无限死亡的梦魇。
Example
```
func fail(message:String):Nothing{
     throw IllegalArgumentException(message)
}
class Person(val name: String?)

func main(){
	val persion=Persion(name = null)
	val s:String=persion.name?:fail("name required")
}

```

##  高阶函数

高阶函数是一类函数，这类函数接受函数为参数，或者把函数当返回值返回。

Koltin支持高阶函数，也就是函数式编程。
>You can perform any operations on functions that are possible for other non-function values.
意思就是说：你可以对函数执行任何原本对非函数可用的任何操作


### 如何调用高阶函数
#### lambdas
lambdas就是一串代码块，用`{}`包裹起来
如果有一个高阶函数的入参是一个函数，那么可以用lambdas来调用它
比如说有一个高阶函数长下面这个样子
```
fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}
```
> 第二个参数就是一个函数，接受两个参数，acc是当前累加结果，element就是下一个元素

用lambdas调用会是下面这个样子
```
val items = listOf(1, 2, 3, 4, 5)  
items.fold(2,{  
    acc:Int,i:Int->  // 当lambdas接受参数的话，它们首先得写上去。然后是->
    val result:Int = acc+i  
    result  //lambdas最后一个表达式被认为是它的返回值
})

```
也可不写lambdas函数参数的类型，编译器可以推断
```
items.fold(2,{a,b->a+b})
```
如果是没有参数的lambdas表达式，代码还可以更简洁

```
{ println("Log message") }
```

lambdas本身就是一个函数，所以你可以写它之后立即调用它
```
println({x:String->x.uppercase()}("hello"))
//HELLO
```
##### trailing lambdas
如果高阶函数只有一个lambda函数参数，那么在调用这个高阶函数，你甚至可以把双括号去掉，只保留lambda特有的大括号
```
val numbers = listOf(1, -2, 3, -4, 5, -6)
val positives = numbers.filter{x -> x >0}
```
如果高阶函数最后一个参数是函数，你在调用高阶函数时可以把lambda放在参数外面
```
println(listOf(1,2,3).fold(0){ x, item->x*item}) // fold 是把list的每一个元素操作叠加
```
Either way,它们都是跟随在函数后面，所以才叫trailing lambdas(尾随lambdas)
##### 带接收者的lambdas
lambdas也可以带接收者，这种情况下，在lambdas里面可以自由的访问接收者的成员函数，变量等。而你不用每次都指定对象来访问它们
最简单带接收者的lambdas是
```
MutableList<Int>.() -> Unit
```
以上，MutableList就是接收者，这个带接收者的lambdas没有参数，也没有返回值
实际使用例子
```
class Canvas {  
    fun drawCircle() = println("🟠 Drawing a circle")  
    fun drawSquare() = println("🟥 Drawing a square")  
}  
  
fun render(block: Canvas.() -> Unit): Canvas {   //带Canvas接收者的lambdas
    val canvas = Canvas()  
    canvas.block()  // 实际使用
    return canvas  
}  
  
fun main() {  
    render {    
        drawCircle()  // 等同于 this.drawCircle() ，this可忽略
        drawSquare()  
    }  
}
```


#### 直接引用成员方法
```
val product = items.fold(1,Int::times) //  times是一个成员函数，参数其实2个，一个是接收者
```

### 函数类型
函数本身也可以作为类型。这样我们就可以写一个变量，类型是函数，值是用lambda写的函数体
比如说
```
val f:(Int)->Int = {a->a+1}
val f:(a:Int)->Int = {a->a+1} //在函数类型上给参数指定参数名，没啥意义主要是给文档用的
```
含义：定义了一个变量，名字叫f，类型是函数，这个函数接受一个数字类型参数，返回数字。
这个函数只是简单地把参数+1然后返回罢了。

上面写的函数类型`(a:Int)->Int` 还可以携带**接收者**
```
val fc : StringBuilder.(s: StringBuilder)-> StringBuilder = {s->append(s)}
```
这样的话就是为StringBuilder对象增加了一个方法，该方法接受StringBuilder参数，返回值是StringBuilder
到时调用就可以用构建StringBuilder对象.fc 来调用它了
> 别看函数体，我为了简单随便写写的。

如果定义的函数类型没有参数，可以更简洁
```
val a ()->Unit
```
可以发现，在这个语法下面，参数和返回值类型是必须指定的，要么是在类型那边指定，要么是在lambdas里面指定，否则编译器会let you down

如果觉得每次变量声明都得写一大串函数声明，也可以先给函数类型一个别名先定义起来
```
typealias ClickHandler = (Button, ClickEvent) -> Unit
```

 
 **嵌套函数类型**
 函数类型里面的参数或者返回值类型也可以是函数（什么嵌套地狱）,用括号包裹起来
 虽然看到会头大，但就是有这种用法
 ```
 val f:(Int)->((String)->String) = {  
    n->{  
        s->s.repeat(n)}  
}
 ```

## 表达式
在Koltin里，万物都可以作为表达式，比如说，`if`语句
```
val s = "is it true"  
val b = if (s == "it's true") true else false
```
when也是行的
```
val s = "Hello"  
val result = when (s) {  
    "I" -> "You"  
    "Hello" -> "Greeting"  
    else -> "Shit"  
}  
println(result)
```
## 单例
在kotlin里面，可以以一个比较方便的方式创建单例对象
举例
```
Object DoAuth{
 fun takeParams(username: String, password: String) {
        println("input Auth parameters = $username:$password")
    }
}
fun main(){
	DoAuth.takeParams("coding_ninja", "N1njaC0ding!")
}
```
以上代码就是创建了一个单例对象，名字叫DoAuth，后面的对象体跟一般的类声明没有区别。
而且只有当takeParams访问时，DoAuth才会创立，也就是懒加载。
单例对象也可以继承，例如
```
interface Auth { fun takeParams(username: String, password: String) }

Object DoAuth: Auth{
	 override fun takeParams(username: String, password: String) {
        println("input Auth parameters = $username:$password")
    }
}
```
也可以创建一个数据类的单例对象，这种对象同样可以使用自动生成的方法如`toString()` 和`equals()`,除了copy不能用，毕竟是单例。
```
data object AppConfig {}
```
### companion对象
类里面可以拥有一个companion对象，这个对象只会在类的第一次被引用时创建，并且所有类的实例都可以访问到这个对象（共享），直接用类来引用也是可以的
其实说白了就是相当于java的类成员和类方法(区分于成员变量和成员方法)
```
class BigBen {
    companion object Bonger {
        fun getBongs(nTimes: Int) {
            repeat(nTimes) { print("BONG ") }
            }
        }
    }
 fun main() {
    // Companion object is created when the class is referenced for the
    // first time.
    BigBen.getBongs(12)
    // BONG BONG BONG BONG BONG BONG BONG BONG BONG BONG BONG BONG 
}   
```
这个对象不需要名称，当然你也可以命名一个。


# 集合(数组)
想简单的创建一个有序数组（范围），可以用以下方式
```
val r = 1..4  //r里面包含1，2，3，4
val r = 1..<4 // r 里面只包含1，2，3
val r = 4 downTo 1 // r里面是4，3，2，1 
//字母也是行的
```
## 常用的集合方法
1. firstNotNullOfOrNull，提供一个lambda表达式，把列表转化为另一个格式的列表
> 比如说从用户数组提取出用户id数组

2. singleOrNull 提供一个lambdas表达式，里面写匹配条件返回ture或者false，返回第一个匹配的
3. listOfNotNull 直接在里面写数组元素，就算是Null也无所谓，最后创建一个非空的列表
4. filterNotNull 从一个列表过滤掉Null元素
5. reduceOrNull 按顺序循环列表，提供当前值和下一值。最后返回叠加后的结果
## while-do循环
这种循环可以先让你执行一段代码do后，再去执行条件表达式
例子
```
var cakeBaked = 0  
do{  
  println("bake a cake")  
  cakeBaked++  
}while (cakeBaked<cakeEaten)
// Bake a cake
// Bake a cake
// Bake a cake
```

# 类
Kotlin支持对象编程，这意味着你可以构建类，创建对象等等。
定义一个类 with properties可以是
```
class Contact(val id:Int,var email:String) //在括号里定义成员变量
```
或者
```
class Contact(val id: Int,  email: String) {  
    val category: String = ""  
}
```
注意：如果在括号里定义成员变量但是不写var或者val，那么这个成员变量不可访问的
你还可以指定成员变量的默认值
```
class Contact(val id:Int,name: String = "同事"){  
    var category:String = "技术部"  
}
```
## 创建对象
不像JAVA,你必须用New 关键字来创建时间，在Kotlin里，你可以简单的用类名来创建实例
```
class Contact(val id:Int,name: String = "同事"){  
    var category:String = "技术部"  
}
val a:Contact = Contact(1,"李四")  //不要把Contact误认为是一般函数了，它是构造函数
```
像上面的代码，在类名后紧跟着写上成员变量，用括号括起来，这样就会生成一个默认构造函数，参数就是那些指定好的成员变量
## 数据类
数据类也是一中类，不同的是编译器会为数据类生成默认的成员函数，比如说
- toString() ： 在`println`会自动使用，不需要特别指定
- equals()：可以使用`==`，不需要真的写equals()
- copy
这样你就不用每次写这些成员函数了。
要声明一个数据类，在一般类的前面添加关键字data

```
data class User(val id:Int,val name: String)
```
注意的是，自动生成的成员函数用到的成员变量，只会是默认构造器里面有的成员变量。
如果成员变量是定义在类body里面的，则不会出现在自动生成的成员函数
## 继承
毕竟是从JAVA衍生过来的，Kotlin也有继承，并且只允许单继承。最上层的类就是`Any`
## 抽象类
抽象类在设计之初就是被继承的，在抽象类里面，你可以拥有抽象的方法和成员变量，也可以拥有实现好的成员变量和方法。一旦继承，你就全部拥有，那么怎么定义抽象类呢？
Example
```
abstract class Product(val name: String, var price: Double) {
    abstract val category: String  //抽象成员变量

    // 抽象类里的具体实现方法
    fun productInfo(): String {
        return "Product: $name, Category: $category, Price: $price"
    }
}
```
要继承的话
```
//继承类可拥有自己的构造函数，这里加了属性warranty，默认构造函数会调父类的构造函数(name, price)
class Electronic(name: String, price: Double, val warranty: Int) : Product(name, price) {
    override val category = "Electronic" //实现了抽象类的category成员变量
}
```

## 接口
和Java特别像，一个类可以实现多个接口，不同的只是语法。
先看看接口的语法
```
interface PaymentMethod {
    // Functions are inheritable by default
    fun initiatePayment(amount: Double): String
}
```
继承的话会有点不同
在类声明后面加上`:`+接口名就可以对这个接口继承，多个用逗号隔开
```
class CreditCardPayment(val cardNumber: String, val cardHolderName: String, val expiryDate: String) : PaymentMethod {
    override fun initiatePayment(amount: Double): String {
        // Simulate processing payment with credit card
        return "Payment of $$amount initiated using Credit Card ending in ${cardNumber.takeLast(4)}."
    }
}
```
## delegation(代理)
如果有一个接口里面规定了许多的抽象方法，想要继承它就必须把所有方法都实现。
但实际上，这个接口有一个字类实现的抽象方法都是通用的，如果这个子类的实现方法可以被其他子类使用，那么它们就不需要实现那么多抽象方法了。
兄弟，有的有的，这个方法叫做delegation
示例
假设我们有这么一个接口
```
interface DrawingTool {
    val color: String
    fun draw(shape: String)
    fun erase(area: String)
    fun getToolInfo(): String
}
```

然后有一个实现
```
class PenTool : DrawingTool {
    override val color: String = "black"

    override fun draw(shape: String) {
        println("Drawing $shape using a pen in $color")
    }

    override fun erase(area: String) {
        println("Erasing $area with pen tool")
    }

    override fun getToolInfo(): String {
        return "PenTool(color=$color)"
    }
}
```
后面如果你想扩展更多的DrawingTool就得重复的实现方法，但是实际上你觉得PenTool里面的实现就够用了，唯一需要定义的就是DrawingTool的颜色。
所以你可以使用delegation
```
class CanvasSession(val tool: DrawingTool) : DrawingTool by tool{
	 override val color: String = "blue"
}
```
这里的`tool`就是PenTool的实例名字,刚好你也把这个实例当作构造函数传进来了不是吗
当然，你还可以继续实现draw、erase、getToolInfo这些方法，只是你不再需要全部实现了
## open类
由于某种原因，你不能写接口或者抽象类，但又想写一个类让别人继承（kotlin一般类不能被继承），这个时候，你可以用open关键字。
```
open class Vehicle(val make: String, val model: String)
```
这样就表示Vehicle是可以被其他类继承的，比如说
```
class car(make:String,model:String,val numberOfDoors:Int):Vehicle(make,model)
```
open类里面的方法也可以标识为open来表示它可以被override。
```
open class Vehicle(val make: String, val model: String) {
    open fun displayInfo() {
        println("Vehicle Info: Make - $make, Model - $model")
    }
}
```
子类可以写override关键字来把它覆盖掉
```
class Car(make: String, model: String, val numberOfDoors: Int) : Vehicle(make, model) {
    override fun displayInfo() {
        println("Car Info: Make - $make, Model - $model, Number of Doors - $numberOfDoors")
    }
}
```
同理，成员变量也可用open来表示可覆盖，但是一般不推荐这么做，你还不如把它都放在构造函数里直接访问。

## sealed class
sealed class 是一类特殊的抽象类，这个类只能被同个包内的子类继承，外包不能继承该类。
举例
```
sealed class Mammal(val name:String){}  
class Cat(val catName:String): Mammal(catName)  
class Human(val humanName:String,val job:String): Mammal(humanName)  
fun greetMammal(mammal: Mammal):String{  
    when (mammal){  
        is Human-> return "Hello ${mammal.name}; You're working as a ${mammal.job}"  
        is Cat -> return "Hello ${mammal.name}"  
    }  
}  
fun main() {  
    println(greetMammal(Cat("Snowy")))  
    // Hello Snowy  
}
```
因为sealed class 的所有子类都在同一个包里，编译器很容易就指定它有哪些字类，所以写when条件判断是很顺手的事。
## 枚举
没啥好说的,语法如下
```
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    YELLOW(0xFFFF00);

    fun containsRed() = (this.rgb and 0xFF0000 != 0)
}
```

这里的枚举可以拥有属性和方法，每一个枚举量其实就是Color的实例

## 内联对象
如果你只想创建一个小对象，并只使用一段很短的时间，那么你可以使用内联对象让JVM编译器对这个对象做点优化来提升性能。
```
@JvmInline
value class Email(val address: String)
```
内联类必不可少的是`@JvmInline`注解和value关键字。
然而需要的注意的是
1. 内联类只能存（value）
2. 内联类的构造函数必须有一个参数
示例
```
// The address property is initialized in the class header.
@JvmInline
value class Email(val address: String)

fun sendEmail(email: Email) {
    println("Sending email to ${email.address}")
}

fun main() {
    val myEmail = Email("example@example.com")
    sendEmail(myEmail)
    // Sending email to example@example.com
}
```

## 成员变量
Kotlin的成员变量和Java很类似，唯一的区别是它为了不再写模板化的get和set方法，都在编译器内自动帮你生成了。
你在Kotlin直接对成员变量读取和赋值，实际编译之后都变成了get和set方法。
那么问题来了，既然直接对成员变量读取和赋值都是调用了get和set方法，那么实际成员变量存储在哪里？
答案是
### Backing fields.
不过Backing fields 存不存在取决于下面任意一个条件
1. 使用的是默认的get和set方法，你不会去覆盖默认的这两个方法
2. 你使用关键字`field`来访问成员变量
不过既然任何访问成员变量最终都会变成调用get和set方法，那么如果你真想修改get或者set方法呢？
这样子搞？
```
class Person {
    var name: String = ""
        set(value) {
            // This causes a runtime error
            name = value.replaceFirstChar { firstChar -> firstChar.uppercase() }
        }
}
```
No No No, 在上面的代码里`name = ` 等同于再次调set这个方法，你就等着无限循环吧
所以为了真正赋值到成员变量里，也就是Backing fields.你得使用Field关键字
```
class Person {
    var name: String = ""
        set(value) {
            field = value.replaceFirstChar { firstChar -> firstChar.uppercase() }
        }
}
```

### extended fields
既然函数有扩展函数，那成员变量也有扩展的咯
示例
```
data class Person(val firstName: String, val lastName: String)
val Person.fullName: String
    get() = "$firstName $lastName"
```
上面的代码就不需要修改Person类的源代码，而凭空增加了一个新的成员变量`fullName`
但是和一般的成员变量不一样的是
1. 编译器不会对`extended fields`生成set和get函数，你得自己写个（写完之后直接对它进行任何操作编译器会自动提供为你写的set和get函数）
2. 实际上extended fields没有backfields，所以实际它存不了任何值, 最多就是计算一些结果。
实际使用示例
```
data class Person(val firstName: String, val lastName: String)

// Extension property to get the full name
val Person.fullName: String
    get() = "$firstName $lastName"

fun main() {
    val person = Person(firstName = "John", lastName = "Doe")

    // Use the extension property
    println(person.fullName)
    // John Doe
}
```
所以总的来说，可以把extended fields当作是成员函数
### delegated fields
成员变量也可以被委托和代理，一般是你觉得简单的backing fieids还不够强大，因为你的成员变量是通过计算得来的结果，简单的赋值和取值已经满足不了你了，
那么你可以把这个变量委托给一个类，让这个类来帮你计算出结果再赋值给这个成员变量。
那这个代理类，就必须有两个或者至少一个函数
```
operator fun getValue(thisRef: Any?, property: KProperty<*>): String {}

operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {}
```
1. thisRef: Any? 这个参数代表是哪个被代理成员变量的对象（到时可以把Any改为更具体的类型）
2. property 这个参数表示是哪个成本变量被代理了，你可以知道它的类型和变量名
3. 如果被代理的成员变量是`val`也就是不会再被赋值，那么setValue函数可以不写
4. operator标记它们是操作函数，这样就可以覆写get和set方法了
实际例子
```
class CachedStringDelegate {
    var cachedValue: String? = null

    operator fun getValue(thisRef: User, property: Any?): String {
        if (cachedValue == null) {
            cachedValue = "${thisRef.firstName} ${thisRef.lastName}"
            println("Computed and cached: $cachedValue")
        } else {
            println("Accessed from cache: $cachedValue")
        }
        return cachedValue ?: "Unknown"
    }
}

class User(val firstName: String, val lastName: String) {
    val displayName: String by CachedStringDelegate()
}
```
这样user的displayName的值就被CachedStringDelegate这个类的getValue函数所代理了。
而且displayName只计算一次，后面你再访问displayName直接返回第一次计算的结果
#### 代理类
还需要咔哧咔哧地写CachedStringDelegate这样的代理类？大人，时代变了，Kotlin提供了一些方便使用的代理类，使用它们你就再也不用写千篇一律代理类和它的get、set方法了
下面是一些常用的代理类
##### Lazy
用Lazy类代理，再写一个lambda表达式，这个表达式的返回值就可以赋值给这个被代理的变量。
用Lazy类代理的好处是，它只在第一次访问这个被代理变量时才赋值
> 如果你不知道这个变量会不会被使用到，那么用lazy是一个不错的方式


举例
```
class Database {
    fun connect() {
        println("Connecting to the database...")
    }

    fun query(sql: String): List<String> {
        return listOf("Data1", "Data2", "Data3")
    }
}

val databaseConnection: Database by lazy {
    val db = Database()
    db.connect()
    db
}

fun fetchData() {
    val data = databaseConnection.query("SELECT * FROM data")
    println("Data: $data")
}

fun main() {
    // First time accessing databaseConnection
    fetchData()
    // Connecting to the database...
    // Data: [Data1, Data2, Data3]

    // Subsequent access uses the existing connection
    fetchData()
    // Data: [Data1, Data2, Data3]
}
```
#####  Observable
这个代理类可以监听变量改变或者赋值
例子如下
```
import kotlin.properties.Delegates.observable

class Thermostat {
    var temperature: Double by observable(20.0) { _, old, new ->
        if (new > 25) {
            println("Warning: Temperature is too high! ($old°C -> $new°C)")
        } else {
            println("Temperature updated: $old°C -> $new°C")
        }
    }
}

fun main() {
    val thermostat = Thermostat()
    thermostat.temperature = 22.5
    // Temperature updated: 20.0°C -> 22.5°C

    thermostat.temperature = 27.0
    // Warning: Temperature is too high! (22.5°C -> 27.0°C)
}
```
observable构造函数的参数就是这个被代理变量的初始值

## 第三方类库
### 导入
导入的语法其实没什么不同。
你可以只导入包里的一个类
```
import kotlin.time.Duration
```
也可以导入这个包里的所有类
```
import kotlin.time.*
```
遗憾的是，`*`是没法导入类里面的companion对象的，所以如果你想用这些对象，你得特别导入
```
import kotlin.time.Duration.Companion.hours
```
### Opt in
有些第三方库的方法可能被标记为需要`opt-in`
也就是说，你在调用它们之前，必须提前标注opt-in才能使用它们。
一般第三方类库的作者觉得这样的方法还在开发中，未来可能会改变。
一般来说，需要opt in标注的方法长这样
```
@ExperimentalUnsignedTypes
inline fun uintArrayOf(vararg elements: UInt): UIntArray
```
如果你有一个方法调用了uintArrayOf，那么你需要在这个方法上面标准opt in
```
@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    // Create an unsigned integer array
    val unsignedArray: UIntArray = uintArrayOf(1u, 2u, 3u, 4u, 5u)

    // Modify an element
    unsignedArray[2] = 42u
    println("Updated array: ${unsignedArray.joinToString()}")
    // Updated array: 1, 2, 42, 4, 5
}
```
可能还有其他opt in的方式，这里就略了。
