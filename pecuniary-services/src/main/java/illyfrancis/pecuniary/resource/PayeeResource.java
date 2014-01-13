package illyfrancis.pecuniary.resource;

import illyfrancis.pecuniary.dao.Payees;
import illyfrancis.pecuniary.dao.Payees.Payee;

import java.util.List;

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

@Path("payees")
public class PayeeResource {

    final static Logger logger = LoggerFactory.getLogger(PayeeResource.class);
    
    public PayeeResource() {
        logger.info("constructor");
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        Payee payee = Payees.get(id);

        if (payee == null) {
            String msg = String.format("no payee found with id [%s]", id);
            logger.info("no payee with id [{}]", id);

            // FIXME - don't use jersey specific exception class
            throw new NotFoundException(msg);
        }

        return Response.ok(payee).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Payee payee) {
        return post(payee);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Payee payee) {
        Payees.put(payee);
        return Response.ok(payee).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") String id) {
        Payees.delete(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {

        List<Payee> payees = Payees.get();

        GenericEntity<List<Payee>> entity = new GenericEntity<List<Payee>>(payees) {
        };
        return Response.ok(entity).build();
    }

}
