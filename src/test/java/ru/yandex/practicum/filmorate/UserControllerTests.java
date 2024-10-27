package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    @Autowired
    private UserController controller;
    @Autowired
    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void addingUser() throws Exception {
        User user = new User(0L,"e@mail", "LOGIN", "Name", LocalDate.of(2000,10,10), null, null);

        String json = objectMapper.writeValueAsString(user);

        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void addingEmptyLoginUser() throws Exception {
        User user = new User(0L,null, null, null, LocalDate.of(2000,10,10), null, null);

        String json = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addingEmptyButLoginUser() throws Exception {
        User user = new User(0L,null, "dsa", null, LocalDate.of(2000,10,10), null, null);

        String json = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addingIncorrectEmailTest() throws Exception {
        User user = new User(0L,"dsabjk?Dsa@", "dsa", "dsadsa", LocalDate.of(2000,10,10), null, null);

        String json = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updatingUser() throws Exception {
        User user = new User(0L,"e@mail",  "LOGIN","Name", LocalDate.of(2000,10,10), null, null);
        String oldJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(oldJson))
                .andExpect(status().isOk());

        User user2 = new User(1L,"e@mail2",  "LOGIN2","Name2", LocalDate.of(2022,11,11), null, null);
        String json2 = objectMapper.writeValueAsString(user2);

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("e@mail2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("LOGIN2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday[1]]").value(11));

        mvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }
}
