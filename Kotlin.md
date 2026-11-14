# Kotlin语法

# 关键字
## 变量相关
- val 意味变量一旦赋值，不可再变（取自英语value，意味着它是一个值，不是变量）
- var 普通的变量声明


# 函数
语法
`func name(a:Int,b:Int)=a+b`
函数是可以写成一个表达式的，你甚至不需要写返回值，编译器自己推断
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
# 模板
Kotlin支持在字符串里面直接运用表达式，到时输出的字符串就会携带这个表达式的结构输出
想要在字符串里面表达这是一个表达式，则在表达式前面用`$` 标记
Example
```
val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"  
  fun getPattern(): String = """\d{2}\s$month\s\d{4}"""
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

上面写的函数类型`(a:Int)->Int` 还可以携带接收者
```
val fc : StringBuilder.(s: StringBuilder)-> StringBuilder = {s->append(s)}
```
这样的话就是为StringBuilder对象增加了一个方法，该方法接受StringBuilder参数，返回值是StringBuilder
到时调用就可以用构建StringBuilder对象.fc 来调用它了
> 别看函数体，我为了简单随便写写的。

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

# 范围
想简单的创建一个有序数组（范围），可以用以下方式
```
val r = 1..4  //r里面包含1，2，3，4
val r = 1..<4 // r 里面只包含1，2，3
val r = 4 downTo 1 // r里面是4，3，2，1 
//字母也是行的
```

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
