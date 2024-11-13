package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepositories.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.utils.Genres;
import ru.yandex.practicum.filmorate.model.utils.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepository.class, FilmRowMapper.class})
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;
    private Film film;
    private List<Genres> genres = new ArrayList<>();
    private Genres genre = new Genres();
    private Mpa mpa = new Mpa();

    @Autowired
    public FilmRepositoryTest(@Qualifier("FilmRepository") FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @BeforeEach
    public void filmCreate() {
        genre.setId(1L);
        genres.add(genre);
        mpa.setId(2L);
        film = new Film(1L,"Name2", "Description2", LocalDate.of(2022,12,12), 100, 0, genres, mpa);
    }

    @Test
    public void testCreatingFilm() {
        Film created = filmRepository.save(film);
        assertThat(created).hasFieldOrPropertyWithValue("id", created.getId());
    }

    @Test
    public void testFindingFilmById() {
        Film created = filmRepository.save(film);

        Optional<Film> searchFilm = filmRepository.findById(created.getId());
        assertThat(searchFilm)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", created.getId()));
    }
}
