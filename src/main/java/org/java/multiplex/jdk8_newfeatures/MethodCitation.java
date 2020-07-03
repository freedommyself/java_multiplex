package org.java.multiplex.jdk8_newfeatures;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * <b>方法引用</b>
 * 与lambda联合使用
 *
 * @since jdk1.8
 */
public class MethodCitation {

    public static void main(String[] args) {
        MethodCitation methodCitation = create(MethodCitation::new);
        MethodCitation methodCitation1 = create(MethodCitation::new);
        List<MethodCitation> list = Arrays.asList(methodCitation);
        System.out.println(list);
        list.forEach(MethodCitation::collide);
        list.forEach(MethodCitation::repair);
        list.forEach(methodCitation1::follow);
    }

    /**
     * 构造器引用
     * {@code Class::new}
     *
     * @param supplier
     * @return
     */
    public static MethodCitation create(Supplier<MethodCitation> supplier) {
        return supplier.get();
    }

    /**
     * 静态方法引用
     * {@code Class::static_method}
     *
     * @param methodCitation
     */
    public static void collide(MethodCitation methodCitation) {
        System.out.println("Collided " + methodCitation.toString());
    }

    /**
     * 特定对象的方法引用
     * {@code instance::method}
     *
     * @param another
     */
    public void follow(MethodCitation another) {
        System.out.println("Following the " + another.toString());
    }

    /**
     * 特定类的任意对象的方法引用
     * {@code Class::method}
     * 方法没有参数
     */
    public void repair() {
        System.out.println("Repaired " + this.toString());
        System.out.println(this);
    }


}
