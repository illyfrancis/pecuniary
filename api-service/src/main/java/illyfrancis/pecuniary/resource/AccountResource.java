package illyfrancis.pecuniary.resource;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("accounts")
@PermitAll
//@RolesAllowed({"admin"})
public class AccountResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context SecurityContext sec) {
        System.out.println("get accounts");
        return Response.ok(new Model()).build();
    }
    
    public static class Model {
        String name = "Acct 1";
        public String getName() {
            return name;
        }
    }
}
