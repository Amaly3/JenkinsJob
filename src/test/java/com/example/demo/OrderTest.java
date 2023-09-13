package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.JWTAuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderTest {
    private OrderController orderController;
    private OrderRepository orderRepo= mock(OrderRepository.class);
    private CartController cartController;
    private ItemController itemController;
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Autowired
    private JacksonTester<ModifyCartRequest> json;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<CreateUserRequest> json1;

    @Before
    public void setup() {
        cartController = new CartController();
        itemController = new ItemController();
        userController = new UserController();
        orderController = new OrderController();
        TestUtils.injectObiects(orderController, "userRepository", userRepo);
        TestUtils.injectObiects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObiects(itemController, "itemRepository", itemRepo);
        TestUtils.injectObiects(userController, "userRepository", userRepo);
        TestUtils.injectObiects(userController, "cartRepository", cartRepo);
        TestUtils.injectObiects(userController, "bCryptPasswordEncoder", encoder);
    }



    @Test
    public void submitOrderWithGenerateAuthToken() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("john");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        // final ResponseEntity<User> response = userController.createUser(r);
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create").content(json1.write(r).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("john");
        request.setQuantity(1);
        request.setItemId(1);
        String token = JWTAuthenticationFilter.createToken("john");
        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart").header("Authorization", token).content(json.write(request).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.post("/api/order/submit/john").header("Authorization", token))
                .andExpect(status().isOk());

    }


}
