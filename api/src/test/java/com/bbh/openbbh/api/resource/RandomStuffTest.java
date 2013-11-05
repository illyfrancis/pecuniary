package com.bbh.openbbh.api.resource;

import static org.junit.Assert.*;

import java.util.List;
import java.util.regex.Pattern;

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
    
    @Test
    public void testRegexIgnoreCase() {
        
        // case sensitive 
        Pattern p = Pattern.compile("ING.*");
        assertTrue(p.matcher("ING").matches());
        assertFalse(p.matcher("ing").matches());
        assertTrue(p.matcher("ING abc").matches());
        assertFalse(p.matcher("ing abc").matches());
        assertFalse(p.matcher("a ING bc").matches());
        assertFalse(p.matcher("a ing bc").matches());
        
        Pattern pi = Pattern.compile("ING.*", Pattern.CASE_INSENSITIVE);
        assertTrue(pi.matcher("ING").matches());
        assertTrue(pi.matcher("ing").matches());
        assertTrue(pi.matcher("ING abc").matches());
        assertTrue(pi.matcher("ing abc").matches());
        assertFalse(pi.matcher("a ING bc").matches());
        assertFalse(pi.matcher("a ing bc").matches());
    }
    
    @Test
    public void testStringFormat() {
        String out = String.format("{%s:{$regex:#}}", "name");
        assertEquals("{name:{$regex:#}}", out);
    }
}
