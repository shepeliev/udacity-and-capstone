package com.familycircleapp.utils;

import android.arch.core.util.Function;
import android.support.annotation.NonNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class F {

  private F() {
    throw new UnsupportedOperationException();
  }

  @NonNull
  public static <T, R> List<R> map(
      @NonNull final Iterable<T> list, @NonNull final Function<T, R> f
  ) {
    final ArrayList<R> r = new ArrayList<>();
    for (final T el : list) {
      r.add(f.apply(el));
    }

    return r;
  }

  @NonNull
  public static <T, R> List<R> map(
      @NonNull final T[] list, @NonNull final Function<T, R> f
  ) {
    final ArrayList<R> r = new ArrayList<>();
    for (final T el : list) {
      r.add(f.apply(el));
    }

    return r;
  }

  public static <T, R> R fold(
      @NonNull final Iterable<T> list, final R initial, @NonNull final Function2<R, T, R> f
  ) {
    R acc = initial;
    for (final T el : list) {
      acc = f.apply(acc, el);
    }
    return acc;
  }

  @NonNull
  public static <K, V> Map.Entry<K, V> mapEntry(@NonNull final K key, @NonNull final V value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }

  @NonNull
  public static <K, V> Map<K, V> mapOf(@NonNull final Iterable<Map.Entry<K, V>> entries) {
    return fold(entries, new HashMap<K, V>(), (acc, el) -> {
      acc.put(el.getKey(), el.getValue());
      return acc;
    });
  }

  public static <T> void foreach(
      @NonNull final Iterable<T> collection, @NonNull Consumer<T> consumer
  ) {
    for (final T el : collection) {
      consumer.accept(el);
    }
  }

  @NonNull
  public static <T> List<T> filter(
      @NonNull final Iterable<T> collection, @NonNull final Predicate<T> predicate
  ) {
    final List<T> resultList = new ArrayList<>();
    F.foreach(collection, el -> {
      if (predicate.test(el)) resultList.add(el);
    });

    return resultList;
  }
}
