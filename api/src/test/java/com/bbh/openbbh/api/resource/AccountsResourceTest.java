package com.bbh.openbbh.api.resource;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import com.bbh.openbbh.api.resource.AccountsResource.SearchResponse;
import com.sun.jersey.core.provider.EntityHolder;

public class AccountsResourceTest {

    // a little fictitious test. so long as mongo's running it'll pass, need to
    // get Accounts to be injected rather than it being static.

    @Test
    @Ignore
    public void testSearch() {
        Query query = new Query();
        EntityHolder<Query> queryHolder = new EntityHolder<Query>(query);
        
        AccountsResource resource = new AccountsResource();
        Response response = resource.search(queryHolder);
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        SearchResponse out = (SearchResponse) response.getEntity();
        assertEquals(10, out.accounts.size());
    }
    
    @Test
    public void testSelect() {
        Query query = new Query();
        EntityHolder<Query> queryHolder = new EntityHolder<Query>(query);
        
        AccountsResource resource = new AccountsResource();
        Response response = resource.select(queryHolder);
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        SearchResponse out = (SearchResponse) response.getEntity();
        assertEquals(10, out.accounts.size());
    }
}
