package com.wm.lock.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by WM on 2015/7/23.
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static int getSize(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <T> boolean isContains(Collection<T> collection, T item) {
        return collection != null && item != null && collection.contains(item);
    }

    public static List mapKeyToList(Map map) {
        return map == null ? null : setToList(map.keySet());
    }

    public static List mapValueToList(Map map) {
        return map == null ? null : new ArrayList(map.values());
    }

    public static <T> List<T> arrayToList(T[] array) {
        return array == null ? null : Arrays.asList(array);
    }

    public static <T> String[] listToArray(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        int size = list.size();
        String[] result = new String[size];
        for (int i=0; i < size; i++) {
            T t = list.get(i);
            result[i] = t == null ? null : t.toString();
        }
        return result;
    }

    public static List setToList(Set set) {
        return set == null ? null : new ArrayList(set);
    }

    public static Set listToSet(List list) {
        return list == null ? null : new HashSet(list);
    }

    public static <E> void diff(Collection<E> target, Collection<E> all, Collection<E> result, Comparator<E> comparator) {
        if (isEmpty(all)) {
            return;
        }

        if (isEmpty(target)) {
            result.addAll(all);
            return;
        }

        for (E item : all) {
            if (!isExists(item, target, comparator)) {
                result.add(item);
            }
        }
    }

    public static <E> boolean isExists(E item, Collection<E> collection, Comparator<E> comparator) {
        for (E allItem : collection) {
            if (comparator == null) {
                if (item == allItem) {
                    return true;
                }
            }
            else {
                if(comparator.compare(item, allItem) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
