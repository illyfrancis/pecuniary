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
import com.sun.jersey.core.provider.EntityHolder;

@Path("accountsfilter")
public class AccountsFilterResource {

    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(EntityHolder<AccountsFilterQuery> query) {
        if (query.hasEntity()) {
            AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query.getEntity());

            List<Model> accounts = Accounts.findBy(mapper.getCriteria(),
                    mapper.getCriteriaValues(),
                    mapper.getLimit(), mapper.getSkip());

            // update its selection based on clients selection
            if (mapper.getChecked().isPresent()) {
                String accountNumbers = mapper.getChecked().get();
                for (Model account : accounts) {
                    if (accountNumbers.indexOf(account.getNumber()) > -1) {
                        account.select(true);
                    }
                }
            }

            long count = Accounts.count(mapper.getCriteria(), mapper.getCriteriaValues());
            return Response.ok(SearchResponse.from(accounts,
                    mapper.getChecked().orNull(), count)).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    @POST
    @Path("select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response select(EntityHolder<AccountsFilterQuery> queryHolder) {
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
    public Response unselect(EntityHolder<AccountsFilterQuery> queryHolder) {
        if (queryHolder.hasEntity()) {
            SearchResponse response = perform("unselect", queryHolder.getEntity());
            return Response.ok(response).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    private AccountsFilterQuery copyQueryAndSetAccountNumbers(AccountsFilterQuery query,
            Set<String> accountNumbers) {

        // copy using the builder
        AccountsFilterQuery.Builder builder = new AccountsFilterQuery.Builder();
        if (query.getFilter() != null) {
            AccountsFilterQuery.Filter filter = query.getFilter();
            builder.setName(filter.getName())
                    .setNumber(filter.getNumber())
                    .setDisplay(filter.getDisplay())
                    .setChecked(Joiner.on(",").join(accountNumbers));
        }

        if (query.getModifier() != null) {
            AccountsFilterQuery.Modifier modifier = query.getModifier();
            builder.setLimit(modifier.getLimit()).setSkip(modifier.getSkip());
        }

        return builder.build();
    }

    private SearchResponse perform(String action, AccountsFilterQuery query) {
        AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query);

        List<Model> accountsFromQuery = Accounts.findBy(mapper.getCriteria(),
                mapper.getCriteriaValues());

        Set<String> selectedAccountNumbers = mapper.getSetOfCheckedAccountNumbers();
        // add any new selection to existing selection.
        for (Model account : accountsFromQuery) {
            if ("select".equalsIgnoreCase(action)) {
                // TODO try functional
                selectedAccountNumbers.add(account.getNumber());
            } else if ("unselect".equalsIgnoreCase(action)) {
                selectedAccountNumbers.remove(account.getNumber());
            }
        }

        AccountsFilterQuery newQuery = copyQueryAndSetAccountNumbers(query,
                selectedAccountNumbers);
        AccountsFilterQueryMapper newMapper = AccountsFilterQueryMapper.of(newQuery);

        List<Model> accounts = Accounts.findBy(newMapper.getCriteria(),
                newMapper.getCriteriaValues(),
                newMapper.getLimit(), newMapper.getSkip());

        // update account's selection based on current selection
        for (Model account : accounts) {
            if (selectedAccountNumbers.contains(account.getNumber())) {
                account.select(true);
            }
        }

        SearchResponse response = SearchResponse.from(accounts,
                Joiner.on(",").join(selectedAccountNumbers),
                Accounts.count(newMapper.getCriteria(), newMapper.getCriteriaValues()));

        return response;
    }

    private static class SearchResponse {
        private List<Model> accounts;
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
