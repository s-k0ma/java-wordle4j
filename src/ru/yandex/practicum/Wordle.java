package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryFile;
import ru.yandex.practicum.exceptions.InvalidUsersInputException;
import ru.yandex.practicum.exceptions.NoHintsAvailableException;
import ru.yandex.practicum.exceptions.WordNoFoundInDictionaryException;
import ru.yandex.practicum.logger.LogLevel;
import ru.yandex.practicum.logger.LoggerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Основной класс приложения
 * Определяет файл словаря
 * Уровень логгирования
 * Обрабатывает действия пользователя
 */
public class Wordle {

    private static final String FILE_PATH = "words_ru.txt";

    private WordleGame game;
    private BufferedReader br;
    private static final LoggerService logger = new LoggerService(LogLevel.INFO);

    public static void main(String[] args) {
        Wordle wordle = new Wordle();
        wordle.run();
    }

    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            WordleDictionary dictionary = WordleDictionaryLoader.loadDictionary(FILE_PATH);
            game = new WordleGame(dictionary);
            play();
        } catch (IOException | EmptyDictionaryFile ex) {
            logger.error(Wordle.class, ex.getMessage());
        }
    }

    public void play() throws IOException {
        logger.info(Wordle.class, "Игра началась");
        while (game.isStepsLeft() && !game.isWin()) {
            System.out.println("Введите слово из пяти букв или нажмите ENTER для подсказки");
            System.out.print("=>");
            String input = br.readLine();
            String result;

            try {
                if (input.isBlank()) result = game.handleHint();
                else {
                    if (!isValidInput(input))
                        throw new InvalidUsersInputException("Вам необходимо ввести слово из 5 букв киррилицей");

                    result = game.handleAttempt(normalizeInput(input));
                }
            } catch (InvalidUsersInputException | NoHintsAvailableException | WordNoFoundInDictionaryException ex) {
                System.out.println(ex.getMessage());
                logger.info(Wordle.class, ex.getMessage());
                continue;
            }

            System.out.println(result);
        }

        if (game.isWin()) System.out.println("Congratulations!!!");
        else System.out.println("GitGud! слово было - " + game.getAnswer());
        logger.info(Wordle.class, "игра закончена, загадано слово - " + game.getAnswer());
        clearConstraints();
    }

    private boolean isValidInput(String input) {
        return input.length() == 5 && input.matches("[а-яёЁ]+");
    }

    private String normalizeInput(String input) {
        return input.toLowerCase(Locale.ROOT).replace("ё", "е");
    }

    public void clearConstraints() {
        game.clearConstraints();
    }
}
