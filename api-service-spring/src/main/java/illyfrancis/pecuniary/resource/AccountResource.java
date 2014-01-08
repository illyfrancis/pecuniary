package illyfrancis.pecuniary.resource;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.google.common.base.Optional;

@Path("accounts")
public class AccountResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // @PermitAll
    // @RolesAllowed({"admin"})
    public Response get(@Context SecurityContext sec) {
        System.out.println("get accounts");
        return Response.ok(new Model()).build();
    }

    @GET
    @Path("settings")
    @Produces(MediaType.APPLICATION_JSON)
    // @PermitAll
    public Response getSettings(@Context SecurityContext sec) {
        Optional<Principal> principal = Optional.fromNullable(sec.getUserPrincipal());
        Principal p = principal.or(new Principal() {
            @Override
            public String getName() {
                return "empty";
            }
        });
        System.out.println("get settings : " + p.getName() + " : " + p.toString());
        return Response.ok("username = " + p.getName()).build();
    }

    public static class Model {
        String name = "Acct 1";

        public String getName() {
            return name;
        }
    }
}
