package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Нужен для преобразования из DTO в сущность, использует конвертер
 */
@Data
public class QuestionDto {
    /**
     * Первый элемент, считывается и устанавливается как вопрос.
     */
    @CsvBindByPosition(position = 0)
    private String text;

    /**
     * Лист ответов на данный вопрос. Ответы создаются при помощи кастомного конвертера.
     */
    @CsvBindAndSplitByPosition(position = 1, collectionType = ArrayList.class, elementType = Answer.class,
            converter = AnswerCsvConverter.class, splitOn = ";")
    private List<Answer> answers;

    public Question toDomainObject() {
        return new Question(text, answers);
    }
}
