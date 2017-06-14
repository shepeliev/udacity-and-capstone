package com.familycircleapp;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class FuncUtilsTest {

  @Test
  public void testMap() throws Exception {
    assertEquals(
        asList("1", "2", "3"),
        FuncUtils.map(asList(1, 2, 3), Object::toString)
    );
  }

  @Test
  public void testReduce() throws Exception {
    final int reduced = FuncUtils.fold(asList(1, 5, 4), 0, (acc, el) -> acc + el);
    assertEquals(10, reduced);
  }

  @Test
  public void testMapOf() throws Exception {
    final Map<String, Integer> expectedMap = new HashMap<String, Integer>() {{
      put("1", 1);
      put("2", 2);
      put("3", 3);
    }};

    assertEquals(expectedMap, FuncUtils.mapOf(asList(
        FuncUtils.mapEntry("1", 1),
        FuncUtils.mapEntry("2", 2),
        FuncUtils.mapEntry("3", 3)
    )));
  }
}
