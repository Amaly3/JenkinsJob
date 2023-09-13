package com.example.demo;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.JWTAuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CartTest {
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
    private JacksonTester<CreateUserRequest> json1;
    @Autowired
    private MockMvc mvc;


    @Before
    public void setup() {
        cartController = new CartController();
        itemController = new ItemController();
        userController = new UserController();
        TestUtils.injectObiects(cartController, "userRepository", userRepo);
        TestUtils.injectObiects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObiects(cartController, "itemRepository", itemRepo);

        TestUtils.injectObiects(itemController, "itemRepository", itemRepo);
        TestUtils.injectObiects(userController, "userRepository", userRepo);
        TestUtils.injectObiects(userController, "cartRepository", cartRepo);
        TestUtils.injectObiects(userController, "bCryptPasswordEncoder", encoder);
    }



    @Test
    public void addToCartWithGenerateAuthToken() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("john");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
       // final ResponseEntity<User> response = userController.createUser(r);
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create").content(json1.write(r).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        //assertNotNull(userRepo.findByUsername("john"));
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
    }


}









