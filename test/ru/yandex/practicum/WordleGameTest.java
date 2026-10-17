package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.WordNoFoundInDictionaryException;

import java.util.List;

public class WordleGameTest {

    private static final List<String> WORDS = List.of(
            "качок", "кабан", "катер", "какао", "кагор",
            "гранд", "кудла", "кагат", "камыш", "карат"
    );

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(WORDS);
        game = new WordleGame(dictionary);
    }

    @Test
    @DisplayName("Начальное состояние: 6 попыток, не выиграно")
    void initialStepsIsSix() {
        Assertions.assertTrue(game.isStepsLeft());
        Assertions.assertFalse(game.isWin());
    }

    @Test
    @DisplayName("Ответ — слово из словаря")
    void answerIsFromDictionary() {
        Assertions.assertTrue(dictionary.isDictionaryContain(game.getAnswer()));
    }

    @Test
    @DisplayName("Слово не из словаря → исключение")
    void attemptWithUnknownWordThrows() {
        Assertions.assertThrows(WordNoFoundInDictionaryException.class,
                () -> game.handleAttempt("абвгд"));
    }

    @Test
    @DisplayName("Невалидное слово НЕ тратит попытку")
    void invalidWordDoesNotDecrementSteps() {
        String guess = pickGuessNotAnswer();
        game.handleAttempt(guess);
        for (int i = 0; i < 3; i++) {
            Assertions.assertThrows(WordNoFoundInDictionaryException.class,
                    () -> game.handleAttempt("абвгд"));
        }

        // всего потрачена 1 попытка из 6
        int attemptsLeft = countRemainingAttempts();
        Assertions.assertEquals(5, attemptsLeft);
    }

    @Test
    @DisplayName("6 валидных попыток → игра завершена")
    void sixAttemptsEndGame() {
        String guess = pickGuessNotAnswer();
        for (int i = 0; i < 6; i++) {
            game.handleAttempt(guess);
        }
        Assertions.assertFalse(game.isStepsLeft());
    }

    @Test
    @DisplayName("Угадывание ответа → победа")
    void correctGuessWins() {
        String answer = game.getAnswer();
        String result = game.handleAttempt(answer);

        Assertions.assertEquals("+++++", result);
        Assertions.assertTrue(game.isWin());
    }

    @Test
    @DisplayName("Победа завершает игру раньше 6 попыток")
    void winEndsGameBeforeSixAttempts() {
        game.handleAttempt(game.getAnswer());
        Assertions.assertTrue(game.isWin());
    }

    @Test
    @DisplayName("Подсказка — слово из словаря")
    void hintIsFromDictionary() {
        String hint = game.handleHint();

        Assertions.assertTrue(
                WORDS.stream().anyMatch(hint::contains),
                "Подсказка не содержит слово из словаря: " + hint
        );
    }

    @Test
    @DisplayName("Подсказка тратит попытку")
    void hintDecrementsSteps() {
        game.handleHint();
        int attemptsLeft = countRemainingAttempts();
        Assertions.assertEquals(5, attemptsLeft);
    }

    /**
     * Возвращает слово из словаря, не равное ответу.
     */
    private String pickGuessNotAnswer() {
        String answer = game.getAnswer();
        return WORDS.stream()
                .filter(w -> !w.equals(answer))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Не нашлось слова, отличного от ответа"));
    }

    /**
     * Считает, сколько ещё попыток осталось, делая «пробные» невалидные ходы.
     * Невалидные ходы не тратят попытки.
     */
    private int countRemainingAttempts() {
        int count = 0;
        while (game.isStepsLeft()) {
            try {
                game.handleAttempt(pickGuessNotAnswer());
            } catch (WordNoFoundInDictionaryException ignored) {
                // не должно случиться — слово из словаря
            }
            count++;
            if (count > 10) break;   // защита
        }
        return count;
    }
}
