package com.evolveum.polygon.connector.racf.rest.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/racf/rest/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GroupService {

    @GET
    RObjects<RGroup> list(@QueryParam("query") String query);

    @GET
    @Path("{id}")
    RGroup get(@PathParam("id") String id);

    @POST
    String add(RGroup user);

    @DELETE
    @Path("{id}")
    void delete(@PathParam("id") String id);

    @PUT
    String update(RGroup user);

    @GET
    @Path("/sync")
    RDeltas sync(@QueryParam("time") Long time);

    @GET
    @Path("/sync/token")
    Long latestToken();
}
