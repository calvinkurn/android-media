package com.tokopedia.track.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Testx {
    public void testAja() {
        // your code goes here
        Test2<B> c = new Test2<>();
        B b = (c.a = new B());
        Type[] interfaces = c.getClass().getGenericInterfaces();

//        ParameterizedType firstInterface = (ParameterizedType) interfaces[0];
//        Class clazz = (Class) firstInterface.getActualTypeArguments()[0];
//        System.out.println(clazz.getName()); // prints "AtomEntry"

        System.out.println(b.getClass().getName());
    }
}

interface A{

}

class B implements A{

}

class Test2<T extends A>{
    T a;
}
