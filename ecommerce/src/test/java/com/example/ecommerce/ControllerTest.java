package com.example.ecommerce;

import com.example.ecommerce.controller.OrderController;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

public class ControllerTest {
    private MockMvc mockMvc;

  //Declaramos los Mocks
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    public void OrderControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }
//Orden Simulada
    @Test
    public void testCreateOrder() throws Exception {
        // Simula la lógica del servicio
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setName("Jhony Existo");
        orderDTO.setProduct("Mockito con hielo");

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        // Define el JSON de la solicitud
        String orderJson = """
            {
                "customerName": "John Doe",
                "product": "Product XYZ"
            }
        """;

        // Realiza la prueba del controlador
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.product", is("Product XYZ")));
    }
}
