package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.security.GrantGivingService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final GrantGivingService grantService;

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<GenreDto> findAll() {
        List<Genre> responseList = genreRepository.findAll();
        return new ArrayList<>(responseList.stream().map(genreMapper::toGenreDto).toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public GenreDto save(GenreDto genreDto) {
        Genre saved = genreRepository.save(genreMapper.toGenre(genreDto));
        //выдача прав
        GenreDto dto = genreMapper.toGenreDto(saved);
        grantService.setReadGrant(dto);
        grantService.setWriteGrant(dto);
        grantService.setCreateGrant(dto);
        grantService.setDeleteGrant(dto);
        grantService.setAdminGrant(dto);

        return dto;
    }
}
