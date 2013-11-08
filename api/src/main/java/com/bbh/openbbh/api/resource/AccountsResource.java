package com.bbh.openbbh.api.resource;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bbh.openbbh.api.dao.Accounts;
import com.bbh.openbbh.api.dao.Accounts.Model;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.sun.jersey.core.provider.EntityHolder;

@Path("accounts")
public class AccountsResource {
    
    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(EntityHolder<Query> queryHolder) {
        if (queryHolder.hasEntity()) {
            AccountsQueryMapper mapper = AccountsQueryMapper
                    .of(queryHolder.getEntity());

            List<Model> accounts = Accounts.findBy(mapper.queryString(), mapper.queryParams(),
                    mapper.limit(), mapper.skip());

            // update its selection based on clients selection
            if (mapper.getChecked().isPresent()) {
                String accountNumbers = mapper.getChecked().get();
                for (Model account : accounts) {
                    if (accountNumbers.indexOf(account.getNumber()) > -1) {
                        account.select(true);
                    }
                }
            }

            return Response.ok(SearchResponse.from(accounts, mapper.getChecked().orNull(),
                    Accounts.count(mapper.queryString(), mapper.queryParams()))).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    @POST
    @Path("select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response select(EntityHolder<Query> queryHolder) {
        if (queryHolder.hasEntity()) {
            SearchResponse response = perform("select", queryHolder.getEntity());
            return Response.ok(response).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    @POST
    @Path("unselect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unselect(EntityHolder<Query> queryHolder) {
        if (queryHolder.hasEntity()) {
            SearchResponse response = perform("unselect", queryHolder.getEntity());
            return Response.ok(response).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    private SearchResponse perform(String action, Query query) {
        AccountsQueryMapper mapper = AccountsQueryMapper.of(query);

        List<Model> accountsFromQuery = Accounts.findBy(mapper.queryString(),
                mapper.queryParams());

        Set<String> selectedAccountNumbers = mapper.checkedAccountNumbers();
        // add any new selection to existing selection.
        for (Model account : accountsFromQuery) {
            if ("select".equalsIgnoreCase(action)) {
                // TODO try functional
                selectedAccountNumbers.add(account.getNumber());
            } else if ("unselect".equalsIgnoreCase(action)) {
                selectedAccountNumbers.remove(account.getNumber());
            }
        }

/*        Query newQuery = copyQueryAndSetAccountNumbers(query, selectedAccountNumbers);
//        Query newQuery = new Query(query);
 */        
        Query newQuery = AccountsQueryMapper.createQueryWith(query, selectedAccountNumbers);
        AccountsQueryMapper newMapper = AccountsQueryMapper.of(newQuery);

        List<Model> accounts = Accounts.findBy(newMapper.queryString(),
                newMapper.queryParams(),
                newMapper.limit(), newMapper.skip());

        // update account's selection based on current selection
        for (Model account : accounts) {
            if (selectedAccountNumbers.contains(account.getNumber())) {
                account.select(true);
            }
        }

        SearchResponse response = SearchResponse.from(accounts,
                Joiner.on(",").join(selectedAccountNumbers),
                Accounts.count(newMapper.queryString(), newMapper.queryParams()));

        return response;
    }

    private Query copyQueryAndSetAccountNumbers(Query query,
            Set<String> accountNumbers) {

        String checked = Joiner.on(",").join(accountNumbers);
        Query copy = new Query(query);
        
        if (Strings.isNullOrEmpty(copy.criteria)) {
            copy.criteria = "{ \"checked\": \"" + checked + "\" }";
        } else {
            // TODO - replace checked
//            copy.criteria;
        }
        
        return copy;
    }

    static class SearchResponse {
        List<Model> accounts;
        private String checked;
        private long total;

        static SearchResponse from(List<Model> accounts, String checked, long total) {
            SearchResponse response = new SearchResponse();
            response.accounts = accounts;
            response.checked = checked;
            response.total = total;
            return response;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("accounts", accounts)
                    .add("selection", checked)
                    .add("total", total)
                    .toString();
        }
    }
}
