package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {

    /**
     * position = 0 -> сюда сохраняется текст вопроса
     */
    @CsvBindByPosition(position = 0)
    private String text;

    /**
     * Берется position сообщение, разделяется по splitOn, формируется в Answer при помощи AnswerCsvConverter
     * Собирается в collectionType
     */
    @CsvBindAndSplitByPosition(position = 1, collectionType = ArrayList.class, elementType = Answer.class,
            converter = AnswerCsvConverter.class, splitOn = ";")
    private List<Answer> answers;

    public Question toDomainObject() {
        return new Question(text, answers);
    }
}
