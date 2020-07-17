package org.java.multiplex.jdk8_newfeatures;

/**
 * @author wangpeng
 * @version 1.0
 * @description
 * @create_time 2020年07月09日 星期四 11:11:27
 */
public interface DefaultAndStaticInterface2 {

    default void say() {
        System.out.println("say2");
    }

    static void speak() {
        System.out.println("speak2");
    }
}
