package com.bbh.openbbh.api.provider;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;


@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Context
    HttpHeaders headers;

    @Override
    public Response toResponse(JsonParseException exception) {
        System.out.println("exception mapper" + exception.toString());
        System.out.println(headers.getAcceptableMediaTypes());
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .build();
    }

}
