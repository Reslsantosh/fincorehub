package com.resustainability.fincorehub.commons;

import java.util.Collection;

public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> boolean isBlank(Collection<T> list) {
        return null == list || list.isEmpty();
    }

    public static <T> boolean isNonBlank(Collection<T> list) {
        return !isBlank(list);
    }
}
