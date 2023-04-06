package base;

import base.entity.Person;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * UnaryOperator<T>：这是一个继承自 Function<T, T> 的子接口，表示接受一个参数并返回一个同类型的结果。它通常用于表示对某个对象进行一元运算，例如取反或求相反数。
 * BiFunction<T, U, R>：这是一个接受两个参数的函数式接口，用于表示一个函数，将两个参数映射为一个结果。它的第一个参数类型是 T，第二个参数类型是 U，返回类型是 R。
 * BinaryOperator<T>：这是一个继承自 BiFunction<T, T, T> 的子接口，表示接受两个同类型的参数并返回一个同类型的结果。它通常用于表示对两个对象进行二元运算，例如加法或乘法。
 * Supplier<T>：这是一个表示无参数函数的接口，用于提供一个值。它通常用于惰性求值或延迟加载数据。
 * Predicate<T>：这是一个表示谓词（判断真假的函数）的接口，用于判断某个条件是否为真。它通常用于筛选集合中的元素或进行条件判断。
 * Function<T, R>：这是一个表示一元函数的接口，用于将某个类型的值转换为另一个类型的值。它通常用于对集合中的元素进行映射或转换。
 * BiConsumer<T, U>：这是一个表示接受两个参数的消费者函数式接口，用于对两个对象进行操作。它通常用于对两个对象进行操作或副作用。
 * Consumer<T>：这是一个表示接受一个参数的消费者函数式接口，用于对某个对象进行操作。它通常用于对集合中的元素进行操作或副作用。
 * Runnable：这是一个表示不接受任何参数且无返回值的函数式接口，用于表示一个操作或任务。它通常用于表示一个线程或定时器任务。
 */
public class Java8FunctionalInterfacesExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 使用Consumer接口打印所有大于5的数
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Consumer<Integer> printGreaterThan5 = number -> {
            if (number > 5) {
                System.out.println(number);
            }
        };
        numbers.forEach(printGreaterThan5);

        // 2. 使用Supplier接口生成斐波那契数列
        Supplier<List<Integer>> fibonacci = () -> {
            int maxNumber = 20;
            List<Integer> sequence = Lists.newArrayList(1, 1);
            while (sequence.size() < maxNumber) {
                int previous = sequence.get(sequence.size() - 1);
                int previous2 = sequence.get(sequence.size() - 2);
                sequence.add(previous + previous2);
            }
            return sequence;
        };
        System.out.println("Fibonacci sequence: " + fibonacci.get());


        // 3. 使用Predicate接口过滤所有长度大于5的字符串
        List<String> strings = Arrays.asList("a", "ab", "abc", "abcd", "abcde", "abcdef", "abcdefg");
        Predicate<String> isLengthGreaterThan5 = s -> s.length() > 5;
        List<String> filteredStrings = strings.stream()
                .filter(isLengthGreaterThan5)
                .collect(Collectors.toList());
        System.out.println("Filtered strings: " + filteredStrings);

        // 4. 使用Function接口将一个数字加倍，并将结果转换为字符串
        Function<Integer, String> doubleAndConvertToString = number -> {
            int doubledNumber = number * 2;
            return String.valueOf(doubledNumber);
        };
        System.out.println("Doubled and converted to string: " + doubleAndConvertToString.apply(10));


        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Bob", 40);
        BiConsumer<Person, String> biConsumer = (person, result) -> System.out.println(result);
        compareAge(person1, person2, biConsumer);

        CompletableFuture<String> future = CompletableFuture.completedFuture("hello!");
        assertEquals("hello!", future.get());
    }

    public static void compareAge(Person p1, Person p2, BiConsumer<Person, String> biConsumer) {
        if (p1.getAge() > p2.getAge()) {
            biConsumer.accept(p1, p1.getName() + " is older.");
        } else {
            biConsumer.accept(p2, p2.getName() + " is older.");
        }
    }

}
