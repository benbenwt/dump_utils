package com.ti.dump_es;

import java.io.File;

public class test {
    public static void main(String[] args) {
        System.out.println(new File(args[0]).exists());
    }
}
