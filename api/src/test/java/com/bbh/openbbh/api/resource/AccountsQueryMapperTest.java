package com.bbh.openbbh.api.resource;

import static com.bbh.openbbh.api.resource.AccountsQueryMapper.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class AccountsQueryMapperTest {

    private static ObjectMapper mapper = new ObjectMapper();

    public String criteriaToJsonString(AccountsQueryMapper.Criteria criteria) {
        String json = null;
        try {
            json = mapper.writeValueAsString(criteria);
        } catch (IOException ex) {

        }
        return json;
    }

    @Test
    public void testGenerateDefaultQuery() {
        Query query = new Query();
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);

        assertNotNull(mapper);
        assertNotNull(mapper.queryString());
    }

    @Test
    public void testDefaultQueryMapsToDefaultLimitAndSkipValues() {
        Query query = new Query();
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        assertEquals(Integer.valueOf(DEFAULT_LIMIT).intValue(), mapper.limit());
        assertEquals(Integer.valueOf(DEFAULT_SKIP).intValue(), mapper.skip());
    }

    @Test
    public void testQueryMapperModifier() {
        Query query = new Query();
        query.limit = "50";
        query.skip = "15";

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        assertEquals(50, mapper.limit());
        assertEquals(15, mapper.skip());
    }

    @Test
    public void testDefaultQueryMapsToAbsentCriteria() {
        Query query = new Query();
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> criteria = mapper.queryString();

        assertFalse(criteria.isPresent());
        assertEquals(0, mapper.queryParams().length);
    }

    @Test
    public void testCriteriaGeneration() {
        Query query = new Query();
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.number = "015";
        criteria.name = "she";
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> queryString = mapper.queryString();

        assertTrue(queryString.isPresent());
        String expected = "{$and:[{name:#},{number:#}]}".replaceAll(" ", "");
        assertEquals(expected, queryString.get());

        Object[] values = mapper.queryParams();
        assertTrue(values.length > 0);
        assertEquals("^she.*", values[0].toString());
        assertEquals("^015.*", values[1].toString());
    }

    @Test
    public void testNumberCriteriaGeneration() {
        Query query = new Query();
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.number = "015";
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> queryString = mapper.queryString();

        assertTrue(queryString.isPresent());
        String expected = "{$and:[{number:#}]}".replaceAll(" ", "");
        assertEquals(expected, queryString.get());

        Object[] values = mapper.queryParams();
        assertTrue(values.length > 0);
        assertEquals("^015.*", values[0].toString());
    }

    @Test
    public void testDefaultQueryMapsToNoCriteria() {
        Query query = new Query();
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);

        assertFalse(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayAllMapsToNoCriteria() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.display = "all";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        assertFalse(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayCheckedMapsToCriteria() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.display = "checked";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        assertTrue(mapper.queryString().isPresent());
    }

    @Test
    public void testQueryWithDisplayCheckedAndWithCheckedValuesMapsToCriteria() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.display = "checked";
        criteria.checked = "1234, 2345, 999";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> queryString = mapper.queryString();
        assertTrue(queryString.isPresent());

        String expected = "{$and: [ {number: {$in:#}} ]}".replaceAll(" ", "");
        assertEquals(expected, queryString.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(1, values.length);
        assertEquals(checkedNumbers, values[0]);
    }

    @Test
    public void testQueryWithDisplayCheckedAndWithUnCheckedValuesMapsToCriteria() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.display = "unchecked";
        criteria.checked = "1234, 2345, 999";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> queryString = mapper.queryString();
        assertTrue(queryString.isPresent());

        String expected = "{$and: [ {number: {$not: {$in:#}}} ]}".replaceAll(" ", "");
        assertEquals(expected, queryString.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(1, values.length);
        assertEquals(checkedNumbers, values[0]);
    }

    @Test
    public void testQueryWithAllFiltersMapsToCriteria() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.number = "015";
        criteria.name = "she";
        criteria.display = "checked";
        criteria.checked = "1234, 2345, 999";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);
        
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        Optional<String> queryString = mapper.queryString();
        assertTrue(queryString.isPresent());

        String expected = "{$and:[{name:#},{number:#},{number:{$in:#}}]}".replaceAll(" ", "");
        assertEquals(expected, queryString.get());

        Object[] values = mapper.queryParams();
        List<String> checkedNumbers = Lists.newArrayList("1234", "2345", "999");
        assertEquals(3, values.length);
        assertEquals("^she.*", values[0].toString());
        assertEquals("^015.*", values[1].toString());
        assertEquals(checkedNumbers, values[2]);
    }

    @Test
    public void testQueryWithCheckedMapsToNoCriteriaAndCheckedString() {
        AccountsQueryMapper.Criteria criteria = new AccountsQueryMapper.Criteria();
        criteria.checked = "1234, 2345, 999";
        Query query = new Query();
        query.criteria = criteriaToJsonString(criteria);

        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);
        assertFalse(mapper.queryString().isPresent());
        assertEquals("1234,2345,999", mapper.getChecked().get());
    }

    @Test
    public void testParseAccountCriteriaCanParseNullJsonString() throws Exception {
        AccountsQueryMapper.Criteria criteria = AccountsQueryMapper.parseAccountCriteria(null);
        assertNotNull(criteria);
    }
    
    @Test
    public void testParseAccountCriteriaCanParseBogusJsonString() throws Exception {
        AccountsQueryMapper.Criteria criteria = AccountsQueryMapper.parseAccountCriteria("//bogus");
        assertNotNull(criteria);
    }
}
