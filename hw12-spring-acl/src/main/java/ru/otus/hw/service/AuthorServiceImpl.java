package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.service.security.GrantGivingService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final GrantGivingService grantService;

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<AuthorDto> findAll() {
        List<Author> responseList = authorRepository.findAll();
        return new ArrayList<>(responseList.stream().map(authorMapper::toAuthorDto).toList());
    }


    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @Transactional
    public AuthorDto save(AuthorDto authorDto) {
        Author saved = authorRepository.save(authorMapper.toAuthor(authorDto));

        AuthorDto dto = authorMapper.toAuthorDto(saved);
        //выдача прав
        grantService.setReadGrant(dto);
        grantService.setWriteGrant(dto);
        grantService.setCreateGrant(dto);
        grantService.setDeleteGrant(dto);
        grantService.setAdminGrant(dto);


        return dto;
    }
}
