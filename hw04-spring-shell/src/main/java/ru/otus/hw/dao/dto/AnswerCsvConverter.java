package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    /**
     * Метод, для разбиения полученного предложения на составные слова и создание из них сущности ответа.
     */
    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
    }
}
