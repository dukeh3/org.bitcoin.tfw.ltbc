package org.bitcoin.tfw.ltbc.webapp;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.bitcoin.tfw.ltbc.LocalTestBlockChain;

@Path("/")
@Singleton
public class LocalBlockChainResource {
	
	LocalTestBlockChain ltbc = new LocalTestBlockChain();

	public LocalBlockChainResource() {
	}

	@Context
	ServletContext context;

	@GET
	@Path("/start")
	@Produces("text/plain")
	public Response start() {
		
		ltbc.startDaemon();
		return Response.ok("Started").build();
	}
	
	@GET
	@Path("/stop")
	@Produces("text/plain")
	public Response stop() {
		
		ltbc.stopDaemon();
		return Response.ok("Stopped").build();
	}
	
	@GET
	@Path("/mine")
	@Produces("text/plain")
	public Response mine(@QueryParam("blocks") Integer blocks) {
		ltbc.mine(blocks);
		return Response.ok("Mined " + blocks + "blocks").build();
	}
	
	@GET
	@Path("/sendto")
	@Produces("text/plain")
	public Response sendTo(@QueryParam("val") Double val, @QueryParam("addr") String addr) {
		ltbc.sendTo(addr, val);
		return Response.ok("Sentind " + val + "to " + addr).build();
	}
}
