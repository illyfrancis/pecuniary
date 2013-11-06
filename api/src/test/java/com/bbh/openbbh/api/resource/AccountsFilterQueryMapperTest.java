package com.bbh.openbbh.api.resource;

import static com.bbh.openbbh.api.resource.AccountsFilterQueryMapper.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class AccountsFilterQueryMapperTest {

    @Test
    public void testGenerateDefaultQuery() {
        AccountsFilterQuery query = new AccountsFilterQuery();
        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);

        assertNotNull(mapper);
        assertNotNull(mapper.queryString());
    }

    @Test
    public void testDefaultQueryMapsToDefaultLimitAndSkipValues() {
        AccountsFilterQuery query = new AccountsFilterQuery();
        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        assertEquals(Integer.valueOf(DEFAULT_LIMIT).intValue(), mapper.limit());
        assertEquals(Integer.valueOf(DEFAULT_SKIP).intValue(), mapper.skip());
    }

    @Test
    public void testQueryMapperModifier() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setLimit("50").setSkip("15").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        assertEquals(50, mapper.limit());
        assertEquals(15, mapper.skip());
    }

    @Test
    public void testDefaultQueryMapsToAbsentCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery();
        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();

        assertFalse(criteria.isPresent());
        assertEquals(0, mapper.queryParams().length);
    }

    @Test
    public void testCriteriaGeneration() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setNumber("015").setName("she").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();

        assertTrue(criteria.isPresent());
        String expected = "{$and:[{name:#},{number:#}]}".replaceAll(" ", "");
        assertEquals(expected, criteria.get());

        Object[] values = mapper.queryParams();
        assertTrue(values.length > 0);
        assertEquals("^she.*", values[0].toString());
        assertEquals("^015.*", values[1].toString());
    }

    @Test
    public void testNumberCriteriaGeneration() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setNumber("015").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();

        assertTrue(criteria.isPresent());
        String expected = "{$and:[{number:#}]}".replaceAll(" ", "");
        assertEquals(expected, criteria.get());

        Object[] values = mapper.queryParams();
        assertTrue(values.length > 0);
        assertEquals("^015.*", values[0].toString());
    }

    @Test
    public void testDefaultQueryMapsToNoCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery();
        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);

        assertFalse(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayAllMapsToNoCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setDisplay("all").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        assertFalse(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayCheckedMapsToCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setDisplay("checked").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        assertTrue(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayCheckedAndWithCheckedValuesMapsToCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setDisplay("checked").setChecked("1234, 2345, 999").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();
        assertTrue(criteria.isPresent());

        String expected = "{$and: [ {number: {$in:#}} ]}".replaceAll(" ", "");
        assertEquals(expected, criteria.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(1, values.length);
        assertEquals(checkedNumbers, values[0]);
    }

    @Test
    public void testQueryWithDisplayCheckedAndWithUnCheckedValuesMapsToCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setDisplay("unchecked").setChecked("1234, 2345, 999").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();
        assertTrue(criteria.isPresent());

        String expected = "{$and: [ {number: {$not: {$in:#}}} ]}".replaceAll(" ", "");
        assertEquals(expected, criteria.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(1, values.length);
        assertEquals(checkedNumbers, values[0]);
    }

    @Test
    public void testQueryWithAllFiltersMapsToCriteria() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder()
                .setNumber("015").setName("she").setDisplay("checked")
                .setChecked("1234, 2345, 999").build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();
        assertTrue(criteria.isPresent());

        String expected = "{$and:[{name:#},{number:#},{number:{$in:#}}]}".replaceAll(" ", "");
        assertEquals(expected, criteria.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(3, values.length);
        assertEquals("^she.*", values[0].toString());
        assertEquals("^015.*", values[1].toString());
        assertEquals(checkedNumbers, values[2]);
    }

    @Test
    public void testQueryWithCheckedMapsToNoCriteriaAndCheckedString() {
        AccountsFilterQuery query = new AccountsFilterQuery.Builder().setChecked("1234, 2345, 999")
                .build();

        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);
        assertFalse(mapper.queryString().isPresent());
        assertEquals("1234,2345,999", mapper.getChecked().get());
    }
}
