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
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.sun.jersey.core.provider.EntityHolder;

@Path("accountsfilter")
public class AccountsFilterResource {

    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(EntityHolder<AccountsFilterQuery> query) {
        if (query.hasEntity()) {
            System.out.println(query.getEntity());

            AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(query.getEntity());

            List<Model> accounts = Accounts.findBy(mapper.getCriteria(), mapper.getValues(),
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

            long count = Accounts.count(mapper.getCriteria(), mapper.getValues());

            System.out.println("count : " + count);

            return Response.ok(SearchResponse.of(accounts,
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
            AccountsFilterQuery query = queryHolder.getEntity();
            System.out.println(query);

            AccountsFilterQueryMapper mapperForSelection = AccountsFilterQueryMapper.of(query);

            // get "all" accounts based on query
            List<Model> selectedAccounts = Accounts.findBy(mapperForSelection.getCriteria(),
                    mapperForSelection.getValues());

            Set<String> selectedAccountNumbers = Sets.newHashSet();
            if (query.getFilter() != null) {
                selectedAccountNumbers = Sets.newHashSet(Splitter.on(",").trimResults().omitEmptyStrings()
                        .split(query.getFilter().getChecked()));
            }

            // produce checked list from the selectedAccounts list
            for (Model account : selectedAccounts) {
                selectedAccountNumbers.add(account.getNumber());
            }

            String accountNumbers = Joiner.on(",").join(selectedAccountNumbers);

            // do a new search for display, build a new query based on original request
            AccountsFilterQuery.Builder builder = new AccountsFilterQuery.Builder();
            if (query.getFilter() != null) {
                AccountsFilterQuery.Filter filter = query.getFilter();
                builder.setName(filter.getName())
                        .setNumber(filter.getNumber())
                        .setDisplay(filter.getDisplay())
                        .setChecked(accountNumbers);
            }

            if (query.getModifier() != null) {
                AccountsFilterQuery.Modifier modifier = query.getModifier();
                builder.setLimit(Long.valueOf(modifier.getLimit()))
                        .setSkip(Long.valueOf(modifier.getSkip()));
            }

            AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(builder.build());

            List<Model> accounts = Accounts.findBy(mapper.getCriteria(), mapper.getValues(),
                    mapper.getLimit(), mapper.getSkip());

            // update its selection based on clients selection
            for (Model account : accounts) {
                if (accountNumbers.indexOf(account.getNumber()) > -1) {
                    account.select(true);
                }
            }

            // count
            long count = Accounts.count(mapper.getCriteria(), mapper.getValues());
            System.out.println("count : " + count);

            return Response.ok(SearchResponse.of(accounts, accountNumbers, count)).build();
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
            AccountsFilterQuery query = queryHolder.getEntity();

            AccountsFilterQueryMapper mapperForSelection = AccountsFilterQueryMapper.of(query);

            // get "all" accounts based on query
            List<Model> selectedAccounts = Accounts.findBy(mapperForSelection.getCriteria(),
                    mapperForSelection.getValues());

            Set<String> selectedAccountNumbers = Sets.newHashSet();
            if (query.getFilter() != null) {
                selectedAccountNumbers = Sets.newHashSet(Splitter.on(",").trimResults().omitEmptyStrings()
                        .split(query.getFilter().getChecked()));
            }

            // produce checked list from the selectedAccounts list
            for (Model account : selectedAccounts) {
                selectedAccountNumbers.remove(account.getNumber());
            }

            String accountNumbers = Joiner.on(",").join(selectedAccountNumbers);

            // do a new search for display, build a new query based on original request
            AccountsFilterQuery.Builder builder = new AccountsFilterQuery.Builder();
            if (query.getFilter() != null) {
                AccountsFilterQuery.Filter filter = query.getFilter();
                builder.setName(filter.getName())
                        .setNumber(filter.getNumber())
                        .setDisplay(filter.getDisplay())
                        .setChecked(accountNumbers);
            }

            if (query.getModifier() != null) {
                AccountsFilterQuery.Modifier modifier = query.getModifier();
                builder.setLimit(Long.valueOf(modifier.getLimit()))
                        .setSkip(Long.valueOf(modifier.getSkip()));
            }

            AccountsFilterQueryMapper mapper = AccountsFilterQueryMapper.of(builder.build());

            List<Model> accounts = Accounts.findBy(mapper.getCriteria(), mapper.getValues(),
                    mapper.getLimit(), mapper.getSkip());

            // update its selection based on clients selection
            for (Model account : accounts) {
                if (accountNumbers.indexOf(account.getNumber()) > -1) {
                    account.select(true);
                }
            }

            // count
            long count = Accounts.count(mapper.getCriteria(), mapper.getValues());
            System.out.println("count : " + count);

            return Response.ok(SearchResponse.of(accounts, accountNumbers, count)).build();
        } else {
            System.out.println("no query");
        }
        return Response.ok().build();
    }

    private static class SearchResponse {
        private List<Model> accounts;
        private String checked;
        private long total;

        static SearchResponse of(List<Model> accounts, String checked, long total) {
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
