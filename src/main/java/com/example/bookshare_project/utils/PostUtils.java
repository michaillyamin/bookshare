package com.example.bookshare_project.utils;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PostUtils {

    public static <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
