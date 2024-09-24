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
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTests {

    @Autowired
    private FilmController controller;
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
    void addingFilm() throws Exception {
        Film film = new Film(0L,"Name", "Description", LocalDate.of(2000,10,10), Duration.ofSeconds(190));

        String json = objectMapper.writeValueAsString(film);

        mvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void addingEmptyFilm() throws Exception {
        Film film = new Film(0L,"321", "", null, null);

        String json = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void addingEmptyNameFilm() throws Exception {
        Film film = new Film(0L,null, "Dsa", null, Duration.ZERO);

        String json = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updatingFilm() throws Exception {
        Film oldFilm = new Film(0L,"Name", "Description", LocalDate.of(2000,10,10), Duration.ofSeconds(190));
        String oldJson = objectMapper.writeValueAsString(oldFilm);

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(oldJson))
                .andExpect(status().isOk());

        Film film2 = new Film(1L,"Name2", "Description2", LocalDate.of(2022,12,12), Duration.ofSeconds(22));
        String json2 = objectMapper.writeValueAsString(film2);

        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(22));

        mvc.perform(get("/films"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }
}
