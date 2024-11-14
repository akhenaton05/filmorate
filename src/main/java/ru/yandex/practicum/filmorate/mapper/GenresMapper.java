package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenresDto;
import ru.yandex.practicum.filmorate.model.utils.Genres;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenresMapper {
    public static GenresDto mapToGenresDto(Genres genre) {
        GenresDto dto = new GenresDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }
}
