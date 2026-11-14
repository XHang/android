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
当它被用在函数上时，通常表示这个函数永远无法正常完成，可能是永远抛出异常或者陷入无限死亡的梦魇。
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
