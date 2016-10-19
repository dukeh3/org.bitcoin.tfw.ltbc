package org.bitcoin.tfw.lbc.webapp;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/lbc/")
@Singleton
public class LocalBlockChainResource {

	public LocalBlockChainResource() {
	}

	@Context
	ServletContext context;

	@GET
	@Path("/hello")
	@Produces("text/plain")
	public Response stop(String adName, @QueryParam("foobar") String foobar) {
		return Response.ok("Hellow Zool" + adName + foobar).build();
	}
}
