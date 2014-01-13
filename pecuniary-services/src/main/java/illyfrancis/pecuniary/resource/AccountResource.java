package illyfrancis.pecuniary.resource;

import illyfrancis.pecuniary.dao.Accounts;
import illyfrancis.pecuniary.dao.Accounts.Account;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("accounts")
@Singleton
public class AccountResource {

    final static Logger logger = LoggerFactory.getLogger(AccountResource.class);

    public AccountResource() {
        logger.info("construct");
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        Account account = Accounts.get(id);

        if (account == null) {
            String msg = String.format("no account found with id [%s]", id);
            logger.info("no account with id [{}]", id);

            // FIXME - don't use jersey specific exception class
            throw new NotFoundException(msg);
        }
        
        logger.info("this object id {}", this.toString());

        return Response.ok(account).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Account account) {
        return post(account);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Account account) {
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

        List<Account> accounts = Accounts.get();

        GenericEntity<List<Account>> entity = new GenericEntity<List<Account>>(accounts) {
        };
        return Response.ok(entity).build();
    }

    /*
     * @GET
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response get(@Context
     * SecurityContext sec) { System.out.println("get accounts");
     * logger.info("in GET accounts"); return Response.ok(new
     * Account()).build(); }
     * 
     * @GET
     * 
     * @Path("settings")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getSettings(@Context SecurityContext sec) { Optional<Principal> principal
     * = Optional.fromNullable(sec.getUserPrincipal()); Principal p =
     * principal.or(new Principal() {
     * 
     * @Override public String getName() { return "empty"; } });
     * System.out.println("get settings : " + p.getName() + " : " +
     * p.toString()); return Response.ok("username = " + p.getName()).build(); }
     */
}
