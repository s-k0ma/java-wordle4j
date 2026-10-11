package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Служебный класс для подготовки игрового словаря
 */
public class WordleDictionaryLoader {

    private static final String EMPTY_DICTIONARY_MSG = "Файл словаря не заполнен! Убедитесь что в словаре прописаны слова длинной 5 символов";
    private static final int WORD_LENGTH = 5;

    /**
     * @param fileName - путь к файлу со словарем
     * @return {@link WordleDictionary} - игровой словарь
     * @throws IOException
     * @throws EmptyDictionaryFile если после фильтрации словарь пуст
     */
    public static WordleDictionary loadDictionary(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            List<String> dictionary = br.lines()
                    .map(String::trim)
                    .filter(str -> str.length() == WORD_LENGTH)
                    .map(String::toLowerCase)
                    .map(str -> str.replace('ё', 'е'))
                    .collect(Collectors.toList());

            if (dictionary.isEmpty()) throw new EmptyDictionaryFile(EMPTY_DICTIONARY_MSG);
            return new WordleDictionary(dictionary);
        }
    }
}
