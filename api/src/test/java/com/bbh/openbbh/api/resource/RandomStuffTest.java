package com.bbh.openbbh.api.resource;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class RandomStuffTest {

    @Test
    public void testMe() {
        String numbers = "123,456";
        
        List<String> list = Lists.newArrayList(
                Splitter.on(',').trimResults().omitEmptyStrings().split(numbers));
        ImmutableList<String> immutable = ImmutableList.copyOf(
                Splitter.on(',').trimResults().omitEmptyStrings().split(numbers));

        assertEquals("123", list.get(0));
        assertEquals("456", list.get(1));

        assertEquals("123", immutable.get(0));
        assertEquals("456", immutable.get(1));
    }

    @Test
    public void testUsingSplitter() {
        String empty = Strings.nullToEmpty(null);
        ImmutableList<String> immutable = ImmutableList.copyOf(
                Splitter.on(',').trimResults().omitEmptyStrings().split(empty));

        assertEquals(0, immutable.size());
    }
}
