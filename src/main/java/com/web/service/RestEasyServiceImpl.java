package com.web.service;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dto.TicketContainer;
import com.dto.TicketListConstraints;
import com.entities.Ticket;
import com.service.Dispatcher;

@Path("/Service")
public class RestEasyServiceImpl {
	private Dispatcher dispatcher;
		
	@GET
	@Produces("application/Json")
	@Path("/{startDate}/{stopDate}")
	public Response getTicketsBetween(@PathParam("startDate") String startDate, 
			@PathParam("stopDate") String stopDate, @Context ServletContext servletContext) {
		ApplicationContext ctx = 
                WebApplicationContextUtils.getWebApplicationContext(servletContext);
		dispatcher= ctx.getBean("dispatcher", Dispatcher.class);
		TicketContainer container = new TicketContainer();
		container.setConstraints(TicketListConstraints.BETWEEN);
		container.setStartDate(startDate);
		container.setStopDate(stopDate);
		return buildResponse(container);
	}
	
	

	@SuppressWarnings("unchecked")
	public Response buildResponse(TicketContainer container) {
		List<Ticket> tickets = null;
		try {
			tickets = (List<Ticket>) dispatcher.service(container);
			container.setTickets(tickets);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tickets != null) {
			GenericEntity<TicketContainer> entity = new GenericEntity<TicketContainer>(container){};
			return Response.ok(entity).build();
		} else {
			return Response.status(200).entity("tickets not found").build();
		}
	}
}
