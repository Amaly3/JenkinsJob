package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
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
public class ItemTest {
    private ItemController itemController;
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Autowired
    private JacksonTester<User> json;
    @Autowired
    private MockMvc mvc;


    @Before
    public void setup() {
        itemController = new ItemController();
        userController = new UserController();
        TestUtils.injectObiects(itemController, "itemRepository", itemRepo);
        TestUtils.injectObiects(userController, "userRepository", userRepo);
        TestUtils.injectObiects(userController, "cartRepository", cartRepo);
        TestUtils.injectObiects(userController, "bCryptPasswordEncoder", encoder);
    }



//
//    public void getToken() throws Exception {
//
//
//       // Car car = getCar();
//        mvc.perform(
//                post(new URI("/login"))
//                        .content(json.write(car).getJson())
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isCreated());
//        mvc.perform(
//                get(new URI("/cars"))
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//    }



    @Test
    public void getItemsWithGenerateAuthToken() throws Exception {
        String token = JWTAuthenticationFilter.createToken("john");
        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/item").header("Authorization", token)).andExpect(status().isOk());
    }

    @Test
    public void getItemByIdWithGenerateAuthToken() throws Exception {
        String token = JWTAuthenticationFilter.createToken("john");
        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/item/1").header("Authorization", token)).andExpect(status().isOk());
        String body = mvc.perform(MockMvcRequestBuilders.get("/api/item/1").header("Authorization", token)).andReturn().getResponse().getContentAsString();
        System.out.println(body);
        assertEquals("{\"id\":1,\"name\":\"Round Widget\",\"price\":2.99,\"description\":\"A widget that is round\"}",body);
    }


    @Test
    public void getItemByNameWithGenerateAuthToken() throws Exception {
        String token = JWTAuthenticationFilter.createToken("john");
        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/item/name/Round Widget").header("Authorization", token)).andExpect(status().isOk());
        String body = mvc.perform(MockMvcRequestBuilders.get("/api/item/name/Round Widget").header("Authorization", token)).andReturn().getResponse().getContentAsString();
        System.out.println(body);
        assertEquals("[{\"id\":1,\"name\":\"Round Widget\",\"price\":2.99,\"description\":\"A widget that is round\"}]",body);
    }


}
