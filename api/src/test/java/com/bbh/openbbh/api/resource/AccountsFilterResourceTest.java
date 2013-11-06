package com.bbh.openbbh.api.resource;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.sun.jersey.core.provider.EntityHolder;

public class AccountsFilterResourceTest {

    // a little fictitious test. so long as mongo's running it'll pass, need to
    // get Accounts to be injected rather than it being static.
    
    @Test
    public void testSearch() {
        AccountsFilterResource resource = new AccountsFilterResource();

        AccountsFilterQuery query = new AccountsFilterQuery.Builder().build();
        EntityHolder<AccountsFilterQuery> queryHolder = new EntityHolder<AccountsFilterQuery>(query);
        Response response = resource.search(queryHolder);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        String x = response.getEntity().getClass().getName();
        System.out.println(x);
    }
}
