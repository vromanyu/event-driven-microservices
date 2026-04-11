package com.vromanyu.product;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;

import java.net.URI;

@Path("/api/v1/products/write")
public class ProductResource {

    @Inject
    ProductService productService;

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Uni<RestResponse<CreateProductResponse>> createProduct(CreateProductRequest createProductRequest) {
        Log.infof("createProduct called with request: %s", createProductRequest);
        return productService.createProduct(Uni.createFrom().item(createProductRequest))
                .map(response -> {
                    var location = uriInfo.getRequestUriBuilder().path(response.productId().toString()).build();
                    return RestResponse.ResponseBuilder.ok(response).location(URI.create(location.toString())).build();
                });
    }
}
