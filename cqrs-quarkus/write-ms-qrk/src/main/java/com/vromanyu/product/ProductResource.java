package com.vromanyu.product;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;

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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update/{productId}")
    public Uni<RestResponse<UpdateProductResponse>> updateProduct(@PathParam("productId") int productId, UpdateProductRequest updateProductRequest) {
        Log.infof("updateProduct called with request: %s for product: %s", updateProductRequest, productId);
        return productService.updateProduct(productId, Uni.createFrom().item(updateProductRequest))
                .map(RestResponse::ok);
    }

    @DELETE
    @Path("/delete/{productId}")
    public Uni<RestResponse<Void>> deleteProduct(@PathParam("productId") int productId) {
        Log.infof("deleteProduct called for product: %s", productId);
        return productService.deleteProduct(productId)
                .map(response -> RestResponse.noContent());
    }
}
