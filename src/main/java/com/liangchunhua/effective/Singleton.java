package com.liangchunhua.effective;

import java.io.Serializable;

public class Singleton implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private static final Singleton singleton = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return singleton;
    }

    private Object readResolve() {
        return singleton;
    }

}