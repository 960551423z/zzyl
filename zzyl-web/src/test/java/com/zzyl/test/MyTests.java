package com.zzyl.test;

import java.util.Objects;

public class MyTests {

    public static void main(String[] args) {

        int result = Objects.hash("123abc","f","dsd","好的");
        int result2 = Objects.hash("123abc","f","dsd","好的");

        System.out.println(result);
        System.out.println(result2);

    }



}