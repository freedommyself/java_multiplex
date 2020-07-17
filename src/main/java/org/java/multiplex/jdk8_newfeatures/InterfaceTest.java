package org.java.multiplex.jdk8_newfeatures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangpeng
 * @version 1.0
 * @description
 * @create_time 2020年07月09日 星期四 11:12:27
 */
public class InterfaceTest implements DefaultAndStaticInterface, DefaultAndStaticInterface2 {

    @Override
    public void say() {
        System.out.println("say3");
    }

    public static void main(String[] args) {
        Map<Integer, List<Integer>> map = Stream.of(1, 3, 3, 2).collect(Collectors.groupingBy(Function.identity()));
        System.out.println(map);
        Map<Integer, Integer> map1 = Stream.of(1, 3, 3, 2).collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(x -> x)));
        System.out.println(map1);
        HashMap<Integer, List<Integer>> hashMap = Stream.of(1, 3, 3, 2).collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.mapping(x -> x + 1, Collectors.toList())));
        System.out.println(hashMap);
    }


}
