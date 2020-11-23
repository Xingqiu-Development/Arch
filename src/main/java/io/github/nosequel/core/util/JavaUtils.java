package io.github.nosequel.core.util;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class JavaUtils {

    /**
     * Get the sorted values of a HashMap
     *
     * @param map        the hash map to get the sorted values of
     * @param comparator the comparator to compare the values with
     * @param <K>        the type of the key in the hashmap
     * @param <V>        the type of the value in the hashmap
     * @return the sorted list of values
     */
    public <K, V> List<V> getSortedValues(Map<K, V> map, Comparator<V> comparator) {
        return new ArrayList<V>(map.values()) {{
            sort(comparator);
        }};
    }
}