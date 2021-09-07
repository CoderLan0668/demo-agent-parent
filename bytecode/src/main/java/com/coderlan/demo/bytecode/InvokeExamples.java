package com.coderlan.demo.bytecode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InvokeExamples {
    public static void main(String[] args) {
        InvokeExamples sc = new InvokeExamples();
        sc.run();
        List<String> list = new LinkedList<>();
        list.add("test");
        List<String> otherList = list.stream().filter(e -> "filtered".equals(e)).collect(Collectors.toList());
        System.out.println(otherList);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "MismatchedQueryAndUpdateOfCollection"})
    private void run() {
        List ls = new ArrayList();
        ls.add("Good Day");

        ArrayList als = new ArrayList();
        als.add("Dydh Da");

    }
}
