package org.vromanyu.query;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vromanyu.core.GetProductListResponse;
import org.vromanyu.core.GetProductResponse;
import org.vromanyu.query.product.ProductController;
import org.vromanyu.query.product.ProductService;

import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    public void whenGetProductById_thenSuccess() throws Exception {
        GetProductResponse getProductResponse = new GetProductResponse(1, "cookies", 100, 10);
        Mockito.when(productService.getProductById(Mockito.anyInt())).thenReturn(getProductResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/get/{id}", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", CoreMatchers.is("cookies")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is(10)));

        Mockito.verify(productService, Mockito.times(1)).getProductById(1);
    }

    @Test
    public void whenGetProductByIdInvalidId_thenFail() throws Exception {
        Mockito.when(productService.getProductById(Mockito.anyInt())).thenThrow(new RuntimeException("product not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/get/{id}", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", CoreMatchers.containsString("product not found")));
    }

    @Test
    public void whenGetAllProducts_thenSuccess() throws Exception {
        GetProductListResponse getProductListResponse = new GetProductListResponse(
                List.of(
                        new GetProductResponse(1, "cookies", 100, 10),
                        new GetProductResponse(2, "donuts", 150, 20)
                )
        );

        Mockito.when(productService.getAllProducts()).thenReturn(getProductListResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/get/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.size()", CoreMatchers.is(2)));
    }

    @Test
    public void whenGetAllProductsAndThereAreNoProducts_thenSuccess() throws Exception {
        GetProductListResponse getProductListResponse = new GetProductListResponse(List.of());

        Mockito.when(productService.getAllProducts()).thenReturn(getProductListResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/get/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.size()", CoreMatchers.is(0)));
    }

}
