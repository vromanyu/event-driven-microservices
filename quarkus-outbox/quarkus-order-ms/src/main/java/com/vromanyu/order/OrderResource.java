package com.vromanyu.order;

import com.vromanyu.dto.CreateOrderRequestDto;
import com.vromanyu.dto.CreateOrderResponseDto;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("/api/order")
public class OrderResource {

    @Inject
    OrderService orderService;

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<CreateOrderResponseDto> createOrder(CreateOrderRequestDto request) {
        Log.infof("createOrder called with request: %s", request);
        CreateOrderResponseDto response = orderService.createOrder(request);
        URI location = uriInfo.getRequestUriBuilder().path("/{orderUuid}").build(response.orderUuid());
        return RestResponse.ResponseBuilder.ok(response).location(URI.create(location.toString())).build();
    }
}
