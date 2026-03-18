package org.vromanyu.write;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;
import org.vromanyu.write.product.ProductController;
import org.vromanyu.write.product.ProductService;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void whenCreateProduct_thenReturnCreatedProduct() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("cookies", 100, 10);
        CreateProductResponse createProductResponse = new CreateProductResponse(1, "cookies", 100, 10);
        Mockito.when(productService.createProduct(createProductRequest)).thenReturn(createProductResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/write/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(createProductRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/products/get/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", CoreMatchers.is("cookies")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is(10)));
    }

    @Test
    public void whenUpdateProductWithValidId_thenReturnUpdatedProduct() throws Exception {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest("cookies", 150, 10);
        UpdateProductResponse updateProductResponse = new UpdateProductResponse(1, "cookies", 150, 10);
        Mockito.when(productService.updateProduct(Mockito.anyInt(), Mockito.eq(updateProductRequest))).thenReturn(updateProductResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/write/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(updateProductRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", CoreMatchers.is("cookies")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(150)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", CoreMatchers.is(10)));
    }

    @Test
    public void whenUpdateProductWithInValidId_thenReturnBadRequest() throws Exception {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest("cookies", 150, 10);
        Mockito.when(productService.updateProduct(Mockito.anyInt(), Mockito.eq(updateProductRequest))).thenThrow(new RuntimeException("product not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/write/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(updateProductRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", CoreMatchers.containsString("product not found")));
    }

    @Test
    public void whenDeleteProductWithValidId_thenReturnNoContent() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(Mockito.anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/write/{id}", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void whenDeleteProductWithInValidId_thenReturnBadRequest() throws Exception {
        Mockito.doThrow(new RuntimeException("product not found")).when(productService).deleteProduct(Mockito.anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/write/{id}", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", CoreMatchers.containsString("product not found")));
    }

}
