import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        for (Map.Entry<String, Long> entry : getMap(RAW_DATA).entrySet()) {
            System.out.println("Key: " + entry.getKey() + "\n" + "Value: " + entry.getValue());
        }

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10
         */

        Integer[] array = new Integer[]{3, 4, 2, 7};
        int sum = 10;
        long from = System.currentTimeMillis();
        System.out.println(from);

        System.out.println(Arrays.toString(getTerms(array, sum)));

        System.out.println(System.currentTimeMillis() - from);

        /*
        Task3
            Реализовать функцию нечеткого поиска

                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */
        System.out.println(fuzzySearch("car", "ca6$$#_rtwheel"));
        System.out.println(fuzzySearch("cwhl", "cartwheel"));
        System.out.println(fuzzySearch("cwhee", "cartwheel"));
        System.out.println(fuzzySearch("cartwheel", "cartwheel"));
        System.out.println(fuzzySearch("cwheeel", "cartwheel"));
        System.out.println(fuzzySearch("lw", "cartwheel"));
    }

    /*
        Метод для Task1
     */

    public static Map<String, Long> getMap(Person[] data) {

        return Arrays.stream(data)
                .flatMap(Stream::ofNullable)
                .distinct()
                .sorted(Comparator.comparingInt(Person::getId))
                .collect(groupingBy(Person::getName, counting()));
    }

    /*
        Метод для Task2
     */
    public static Integer[] getTerms(Integer[] array, int sum) {

//        Map<Integer, Integer> integerMap = new HashMap<>();
//
//        for(Integer integer: array) {
//            Integer search = sum - integer;
//            if(integerMap.containsKey(integer)) {
//                return new Integer[] {integerMap.get(integer), integer};
//            } else {
//                integerMap.put(search, integer);
//            }
//        }

        return Arrays.stream(array)//создаем первый поток из массива чисел переданных в метод
                .flatMap(Stream::ofNullable)// предотвращаем попадание null
                .flatMap(integerFromFirstStream -> //берем первое число из потока
                        Arrays.stream(array) //создаем второй поток из массива чисел переданных в метод
                                .flatMap(Stream::ofNullable)// предотвращаем попадание null
                                .skip(1) //пропускаем первое число из потока, для исключения сложения первого числа с самим собой
                                .filter(integerFromSecondStream -> integerFromFirstStream + integerFromSecondStream == sum) //проверяем на выполнение условия
                                .map(secondInteger -> new Integer[]{integerFromFirstStream, secondInteger}))// при выполнении условия числа из первого и второго потоков складываем в массив
                .findFirst() // получаем первый массив со значениями
                .orElse(new Integer[]{0,0}); //при не выполнении условий - вовращаем массив заполненный 00
//        return new Integer[]{0,0};
    }

    /*
        Метод для Task3
     */
    public static boolean fuzzySearch(String word, String lotsOf) {

        if (word == null || lotsOf == null) return false;

        final int[] previousCharPosition = {0};

        Function<Integer, Integer> function = charFromWord -> {
            for (int currentCharPosition = previousCharPosition[0]; currentCharPosition < lotsOf.length(); currentCharPosition++) {
                if (charFromWord == lotsOf.charAt(currentCharPosition)) {

                    previousCharPosition[0] = ++currentCharPosition;
                    return charFromWord;
                }
            }
            return -1;
        };

        int[] res = word.chars()
                .map(function::apply)
                .filter(i -> i != -1)
                .toArray();

        return Arrays.equals(word.chars().toArray(), res);
    }
}
