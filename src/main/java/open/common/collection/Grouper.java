package open.common.collection;

import open.common.collection.annotation.GroupKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class Grouper {
    public static <E> Map<String, List<E>> group(List<E> iteratee) {
        return group(iteratee, "key");
    }

    public static <E> Map<String, List<E>> group(List<E> iteratee, String groupKeyName) {
        Map<String, List<E>> result = new HashMap<>();
        if (iteratee.size() == 0) {
            return result;
        }

        Class clazz = iteratee.get(0).getClass();
        Method[] methods = clazz.getDeclaredMethods();

        // Find GroupKey annotated method
        Method groupKeyMethod = null;
        for (Method method : methods) {
            GroupKey groupKey = method.getAnnotation(GroupKey.class);
            if (groupKey != null) {
                if (groupKey.name().equals(groupKeyName)) {
                    isStringReturnType(method);
                    if (groupKeyMethod != null) {
                        throw new IllegalArgumentException("GroupKey name \"" + groupKeyName + "\" duplicate!");
                    }
                    groupKeyMethod = method;
                }
            }
        }

        // Use group key method to manipulate group
        try {
            for (E each : iteratee) {
                String key = (String) groupKeyMethod.invoke(each);
                if (result.containsKey(key)) {
                    result.get(key).add(each);
                } else {
                    List<E> group = new ArrayList<>();
                    group.add(each);
                    result.put(key, group);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); // cannot not occurred
            throw new RuntimeException("Grouper.group reflection exception!");
        }

        return result;
    }

    private static void isStringReturnType(Method method) {
        if (!method.getReturnType().getTypeName().equals("java.lang.String")) {
            throw new IllegalArgumentException("GroupKey return type must be java.lang.String!!");
        }
    }
}
