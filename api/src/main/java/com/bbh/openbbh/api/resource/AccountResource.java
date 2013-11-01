package com.bbh.openbbh.api.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bbh.openbbh.api.dao.Accounts;
import com.bbh.openbbh.api.dao.Accounts.Model;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.provider.EntityHolder;

@Path("accounts")
public class AccountResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        Model account = Accounts.get(id);

        if (account == null)
            throw new NotFoundException();

        return Response.ok(account).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Model account) {
        return post(account);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Model account) {
        Accounts.put(account);
        return Response.ok(account).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") String id) {
        Accounts.delete(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {

        List<Model> accounts = Accounts.get();

        GenericEntity<List<Model>> entity = new GenericEntity<List<Model>>(
                accounts) {
        };
        return Response.ok(entity).build();
    }

    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(
            @DefaultValue("10") @QueryParam("limit") Integer limit,
            @DefaultValue("0") @QueryParam("skip") Integer skip,
            EntityHolder<Auxiliary> auxiliary) {

        // TODO - exception handler for JsonParseException for invalid json
        // format
        // TODO - handler for JsonMappingException for when aux is not string
        // (e.g. an array)

        // Auxiliary is optional.
        Optional<String> aux = Optional.absent();
        if (auxiliary.hasEntity()) {
            aux = Optional.fromNullable(auxiliary.getEntity().aux);
        }

        // fetch accounts
        List<Model> accounts = Accounts.get(limit, skip);

        // update its selection based on clients selection
        if (aux.isPresent()) {
            String accountNumbers = aux.get();
            for (Model account : accounts) {
                if (accountNumbers.indexOf(account.getNumber()) > -1) {
                    account.select(true);
                }
            }
        }

        return Response.ok(SearchResponse.of(accounts, aux.orNull(), Accounts.count())).build();
    }

    @POST
    @Path("select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response select(
            @DefaultValue("10") @QueryParam("limit") Integer limit,
            @DefaultValue("0") @QueryParam("skip") Integer skip,
            EntityHolder<SearchQuery> searchQuery) {

        // SearchQuery is optional.
        Optional<String> aux = Optional.absent();
        Optional<Query> query = Optional.absent();
        if (searchQuery.hasEntity()) {

            System.out.println(searchQuery.getEntity().toString());

            aux = Optional.fromNullable(searchQuery.getEntity().aux);
            query = Optional.fromNullable(searchQuery.getEntity().query);
        }

        // fetch accounts
        List<Model> accounts = findAccounts(query, limit, skip);

        // TODO - need to 'select' everything

        // update its selection based on clients selection
        if (aux.isPresent()) {
            String accountNumbers = aux.get();
            for (Model account : accounts) {
                if (accountNumbers.indexOf(account.getNumber()) > -1) {
                    account.select(true);
                }
            }
        }

        return Response.ok(SearchResponse.of(accounts, aux.orNull(), Accounts.count())).build();
    }

    private List<Model> findAccounts(Optional<Query> query, Integer limit, Integer skip) {

        Optional<String> number = Optional.absent();
        Optional<String> name = Optional.absent();
        Optional<String> include = Optional.absent();
        
        if (query.isPresent()) {
            Query q = query.get();
            number = Optional.fromNullable(q.number);
            name = Optional.fromNullable(q.name);
            include = Optional.fromNullable(q.include);
        }

        return Accounts.findBy(number, name, include, limit, skip);
    }

    private static class Auxiliary {
        private String aux;
    }

    private static class Query {
        private String number;
        private String name;
        private String include;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("number", number).add("name", name)
                    .add("include", include).toString();
        }
    }

    private static class SearchQuery {
        private Query query;
        private String aux;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("query", query).add("aux", aux).toString();
        }
    }

    private static class SearchResponse {
        private List<Model> accounts;
        private String aux;
        private long total;

        static SearchResponse of(List<Model> accounts, String aux, long total) {
            SearchResponse response = new SearchResponse();
            response.accounts = accounts;
            response.aux = aux;
            response.total = total;
            return response;
        }

        // TODO - figure out why this doesn't work
        // @JsonCreator
        // private SearchResponse(@JsonProperty("accounts") List<Model>
        // accounts,
        // @JsonProperty("aux") String aux) {
        // this.accounts = accounts;
        // this.aux = aux;
        // }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("accounts", accounts)
                    .add("selection", aux)
                    .add("total", total)
                    .toString();
        }
    }
}
