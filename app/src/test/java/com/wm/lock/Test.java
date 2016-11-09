package com.wm.lock;

import org.junit.Before;

import java.util.Calendar;
import java.util.Date;

public class Test {

    @Before
    public void setup() {
        System.out.println(".......setup......");
    }

    @org.junit.Test
    public void test() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 1, 1);
        Date date1 = c.getTime();
        Date date2 = new Date();
        System.out.println("date1: " + date1.getTime());
        System.out.println("date2: " + date2.getTime());
    }

}
