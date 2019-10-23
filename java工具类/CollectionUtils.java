import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class CollectionUtilsDemo {
    public static void main(String[] args) {
        String[] arrayA = new String[] { "A", "B", "C", "D", "E", "F" };
        String[] arrayB = new String[] { "B", "D", "F", "G", "H", "K" };
        List<String> listA = Arrays.asList(arrayA);
        List<String> listB = Arrays.asList(arrayB);
        CollectionUtils.isEmpty(listA);

        System.out.println(ArrayUtils.toString(CollectionUtils.union(listA, listB)));

        System.out.println(ArrayUtils.toString(CollectionUtils.intersection(listA, listB)));

        System.out.println(ArrayUtils.toString(CollectionUtils.disjunction(listA, listB)));

        System.out.println(ArrayUtils.toString(CollectionUtils.subtract(listA, listB)));

        System.out.println(CollectionUtils.isEqualCollection(listA,listB));
    }
}
