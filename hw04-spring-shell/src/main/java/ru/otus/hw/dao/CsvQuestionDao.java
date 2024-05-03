package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    /**
     * Считывание вопросов из файлика
     */
    @Override
    public List<Question> findAll() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream resourceAsStream = Objects.requireNonNull(
                classLoader.getResourceAsStream(fileNameProvider.getTestFileName()))
        ) {

            CSVReader csvReader = getReader(resourceAsStream, getParser());

            List<QuestionDto> parsed = new CsvToBeanBuilder<QuestionDto>(csvReader)
                    .withType(QuestionDto.class)
                    .build()
                    .parse();

            return parsed.stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            String msg = "Произошла ошибка при работе с CSV файлом.";
            throw new QuestionReadException(msg, e);
        }
    }

    private CSVParser getParser() {
        return new CSVParserBuilder()
                .withSeparator('|')
                .build();
    }

    private CSVReader getReader(InputStream inputStream, CSVParser parser) {
        return new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();
    }
}
