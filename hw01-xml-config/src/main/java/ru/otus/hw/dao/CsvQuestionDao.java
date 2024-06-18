package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exception.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

/**
 * Реализация API для считывания вопросов.
 */
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    /**
     * Имя файла с вопросами, предоставляется конфигурацией.
     */
    private final TestFileNameProvider fileNameProvider;


    /**
     * Считывание вопросов.
     */
    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream resourceAsStream = classLoader.getResourceAsStream(fileNameProvider.getTestFileName())) {


            CSVReader csvReader = getReader(Objects.requireNonNull(resourceAsStream), getParser());

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

    /**
     * Указание разделителя и создание парсера.
     */
    private CSVParser getParser() {
        return new CSVParserBuilder()
                .withSeparator('|')
                .build();
    }

    /**
     * Тут происходит пропуск первой строчки и создание ридера.
     */
    private CSVReader getReader(InputStream inputStream, CSVParser parser) {
        return new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();
    }
}
