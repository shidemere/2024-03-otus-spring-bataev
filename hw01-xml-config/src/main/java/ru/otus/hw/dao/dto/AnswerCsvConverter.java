package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;

/**
 * Конвертер, используемый при чтении ответов из строк CSV файла.
 */
public class AnswerCsvConverter extends AbstractCsvConverter {

    /**
     * Принимает каждый элемент строки, расценивающийся как ответ и создаёт на его основе Answer.
     *
     * @return объект Answer, созданный из строки.
     */
    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
    }
}
