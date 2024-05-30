package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Comment;

import java.util.List;


public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBookId(String id);


}
