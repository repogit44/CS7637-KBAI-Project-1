package solver;

import java.util.HashSet;
import java.util.Set;

public class SetExtensions {

    public static <T> Set<T> Difference(Set<T> set1, Set<T> set2) {
        Set<T> set1Copy = new HashSet<T>(set1);
        set1Copy.removeAll(set2);
        return set1Copy;
    }

    public static <T> Set<T> Intersection(Set<T> set1, Set<T> set2) {
        Set<T> set1Copy = new HashSet<T>(set1);
        set1Copy.retainAll(set2);
        return set1Copy;
    }
}
