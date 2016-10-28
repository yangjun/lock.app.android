package com.wm.lock;

import org.junit.Assert;
import org.junit.Before;

public class Test {

    @Before
    public void setup() {
        System.out.print(".......setup......");
    }

    @org.junit.Test
    public void test() {
        Assert.assertEquals(6, 6);
    }

}
