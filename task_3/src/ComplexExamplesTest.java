import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ComplexExamplesTest {

    @Test
    public void test_getMap() {
        ComplexExamples.Person[] RAW_DATA = new ComplexExamples.Person[]{
                new ComplexExamples.Person(0, "Harry"),
                new ComplexExamples.Person(0, "Harry"), // дубликат
                new ComplexExamples.Person(1, "Harry"), // тёзка
                new ComplexExamples.Person(2, "Harry"),
                new ComplexExamples.Person(3, "Emily"),
                new ComplexExamples.Person(4, "Jack"),
                new ComplexExamples.Person(4, "Jack"),
                new ComplexExamples.Person(5, "Amelia"),
                new ComplexExamples.Person(5, "Amelia"),
                new ComplexExamples.Person(6, "Amelia"),
                new ComplexExamples.Person(7, "Amelia"),
                new ComplexExamples.Person(8, "Amelia"),
        };

        Map<String, Long> exeption = new HashMap<String, Long>();
        exeption.put("Amelia", 4L);
        exeption.put("Emily", 1L);
        exeption.put("Harry", 3L);
        exeption.put("Jack", 1L);

//        assertTrue(ComplexExamples.getMap(RAW_DATA).contain("Amelia"));

    }

    @Test
    public void test_getTerms() {

        Integer[] array = new Integer[]{3, 4, 2, 7};
        int sum = 10;

        Integer[] exepted = new Integer[]{3, 7};

//        assertArrayEquals(ComplexExamples.getTerms(array, sum), exepted);
    }

    @Test
    public void test_fuzzySearch() {
        assertTrue(ComplexExamples.fuzzySearch("car", "ca6$$#_rtwheel"));
        assertTrue(ComplexExamples.fuzzySearch("cwhl", "cartwheel"));
        assertTrue(ComplexExamples.fuzzySearch("cwhee", "cartwheel"));
        assertTrue(ComplexExamples.fuzzySearch("cartwheel", "cartwheel"));
        assertFalse(ComplexExamples.fuzzySearch("cwheeel", "cartwheel"));
        assertFalse(ComplexExamples.fuzzySearch("lw", "cartwheel"));
    }
}