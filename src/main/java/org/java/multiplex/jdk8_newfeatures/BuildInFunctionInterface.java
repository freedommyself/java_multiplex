package org.java.multiplex.jdk8_newfeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <b>Java内置的四大核心函数式接口</b>
 *
 * @since jdk8
 */
public class BuildInFunctionInterface {

    public static void main(String[] args) {
        int type = 4;
        switch (type) {
            case 1:
                testConsumer();
                break;
            case 2:
                testSupplier();
                break;
            case 3:
                testFunction();
                break;
            case 4:
                testPredicate();
        }
    }

    /**
     * Consumer<T> 消费型接口
     * {@code accept(T t)}
     *
     * @param n
     * @param consumer
     * @see Consumer#accept(Object)
     */
    public static void consume(Integer n, Consumer<Integer> consumer) {
        consumer.accept(n);
    }

    /**
     * Supplier<T> 供给型接口
     * {@code T get()}
     *
     * @param n
     * @param supplier
     * @return
     * @see Supplier#get()
     */
    public static List<Integer> supplier(int n, Supplier<Integer> supplier) {
        List<Integer> numList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numList.add(supplier.get());
        }
        return numList;
    }

    /**
     * Function<R,T> 函数型接口
     * {@code R apply(T t)}
     *
     * @param str
     * @param function
     * @return
     * @see Function#apply(Object)
     */
    public static String function(String str, Function<String, String> function) {
        return function.apply(str);
    }

    /**
     * Predicate<T> 断言型接口
     * {@code boolean test(T t)}
     *
     * @param strings
     * @param predicate
     * @return
     * @see Predicate#test(Object)
     */
    public static List<String> predicate(List<String> strings, Predicate<String> predicate) {
        List<String> list = new ArrayList<>();
        for (String s : strings) {
            if (predicate.test(s)) {
                list.add(s);
            }
        }
        return list;
    }

    public static void testConsumer() {
        consume(1000, d -> System.out.println(d));
    }

    public static void testSupplier() {
        List<Integer> numList = supplier(10, () -> (int) (Math.random() * 101));
        for (Integer i : numList) {
            System.out.println(i);
        }
    }

    public static void testFunction() {
        String str = function("func", s -> s.toUpperCase());
        System.out.println(str);
    }

    public static void testPredicate() {
        List<String> s1 = predicate(Arrays.asList("huZi", "adAAd", "1231", "414441", "gaGs"), s -> s.length() > 3);
        System.out.println(s1);
        List<String> s2 = predicate(Arrays.asList("huZi", "adAAd", "1231", "414441", "gaGs"), s -> s.contains("d"));
        System.out.println(s2);
    }

}
