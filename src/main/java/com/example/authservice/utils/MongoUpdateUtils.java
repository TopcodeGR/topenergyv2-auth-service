package com.example.authservice.utils;

import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MongoUpdateUtils<T> {

    private T o;

    public MongoUpdateUtils(T o) {
        this.o = o;
    }


    public Update generateUpdate() throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        Update update = new Update();


        for(Field f: fields){
            f.setAccessible(true);
            Object value = f.get(o);

            if(value!=null){
                update.set(f.getName(),value);
            }
        }

        return update;


    }
}
