package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        List<Author> responseList = authorRepository.findAll();
        return responseList.stream().map(authorMapper::toAuthorDto).toList();
    }

    @Override
    public AuthorDto save(AuthorDto authorDto) {
        Author saved = authorRepository.save(authorMapper.toAuthor(authorDto));
        return authorMapper.toAuthorDto(saved);
    }
}
