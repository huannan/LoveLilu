package com.lovelilu;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        Gson gson = new Gson();
        T t = gson.fromJson("{'i' : 1}", T.class);
        System.out.println(t.getI());
    }


    class T {
        private int i;

        public int getI() {
            return i;
        }
    }
}