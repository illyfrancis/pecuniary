package com.bbh.openbbh.api.resource;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class ObjectMapperTest {

    @Test
    public void testFoo() throws IOException {

        String filterJson = new StringBuilder("{")
                .append("'number': '015'").append(",")
                .append("'name': 'she'").append(",")
                .append("'display': 'checked'")
                .append("}")
                .toString()
                .replaceAll("'", "\"");

        ObjectMapper mapper = new ObjectMapper();
        AccountsQueryMapper.Criteria filter = mapper.readValue(filterJson, AccountsQueryMapper.Criteria.class);
        assertEquals("015", filter.number);
        assertEquals("she", filter.name);
        assertEquals("checked", filter.display);
        assertNull(filter.checked);
    }
}
