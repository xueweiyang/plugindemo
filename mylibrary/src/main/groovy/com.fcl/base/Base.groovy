package com.fcl.base

int method(String arg) {
    return 1
}

int method(Object arg) {
    return 2
}

java.lang.Object o = "Object"
println("result"+method(o))

public class Person {
    String name
}

//def name = "zhangsan"
//println("type:"+name.class)



def list=[1,3,"name"]
list.each {val->
    println("val:${val}")
}
def map = [name:"wang", age:99]
map.each { key, val->
    println("key:${key},val:${val}")
}
def repeat() {
    for (i in 0..2) {
        println(i)
    }
}
repeat()

def testClosure(int num, Closure closure) {
    closure(num)
}
Closure closure = { num->
    println("test:${num}")
}
testClosure(5, closure)