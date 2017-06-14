package com.familycircleapp;

import com.familycircleapp.utils.F;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class FTest {

  @Test
  public void testMap() throws Exception {
    assertEquals(
        asList("1", "2", "3"),
        F.map(asList(1, 2, 3), Object::toString)
    );
  }

  @Test
  public void testReduce() throws Exception {
    final int reduced = F.fold(asList(1, 5, 4), 0, (acc, el) -> acc + el);
    assertEquals(10, reduced);
  }

  @Test
  public void testMapOf() throws Exception {
    final Map<String, Integer> expectedMap = new HashMap<String, Integer>() {{
      put("1", 1);
      put("2", 2);
      put("3", 3);
    }};

    assertEquals(expectedMap, F.mapOf(asList(
        F.mapEntry("1", 1),
        F.mapEntry("2", 2),
        F.mapEntry("3", 3)
    )));
  }
}
