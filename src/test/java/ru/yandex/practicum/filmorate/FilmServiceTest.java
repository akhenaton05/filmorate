package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.utils.Genres;
import ru.yandex.practicum.filmorate.model.utils.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {
    private final FilmService filmService;
    private Film film;
    private List<Genres> genres = new ArrayList<>();
    private Genres genre = new Genres();
    private Mpa mpa = new Mpa();


    @BeforeEach
    public void filmCreate() {
        genre.setId(1L);
        genres.add(genre);
        mpa.setId(2L);
        film = new Film(1L,"Name2", "Description2", LocalDate.of(2022,12,12), 100, 0, genres, mpa);
    }

    @Test
    public void testCreatingFilm() {
        FilmDto dto = filmService.createFilm(film);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("name", "Name2");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("description", "Description2");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("duration", 100);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("mpa.name", "PG");
    }

    @Test
    public void testGettingFilmById() {
        FilmDto dto = filmService.createFilm(film);
        FilmDto dtoId = filmService.getFilmById(film.getId());
        assertThat(dtoId)
                .hasFieldOrPropertyWithValue("id", film.getId())
                .hasFieldOrPropertyWithValue("name", "Name2");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("rate", 0);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("mpa.name", "PG");
        assertThat(dto.getGenres())
                .extracting(Genres::getName)
                .contains("Комедия");
    }

}
