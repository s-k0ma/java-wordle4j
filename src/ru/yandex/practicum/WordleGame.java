package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordNoFoundInDictionaryException;

/**
 * Основной класс игры
 * Реализует взаимодействие со словарем
 * Управляет логикой игровых параметров
 */
public class WordleGame {

    private static final int MAX_STEPS = 6;
    private static final String NO_WORD_FOUND_MSG = "Такого слова нет в словаре - попробуйте другое";

    private final String answer;
    private final WordleDictionary dictionary;
    private int steps;
    private boolean isWin;

    public WordleGame(WordleDictionary dictionary) {
        this.steps = MAX_STEPS;
        this.dictionary = dictionary;
        this.answer = dictionary.pickAnswer();
    }

    public String getAnswer() {
        return answer;
    }

    public String handleAttempt(String input) {
        if (!dictionary.isDictionaryContain(input))
            throw new WordNoFoundInDictionaryException(NO_WORD_FOUND_MSG);

        if (input.equals(answer)) {
            isWin = true;
        }
        String result = dictionary.compareAssuming(input, answer);
        dictionary.addConstraint(input, result);
        steps--;
        return result;
    }

    public String handleHint() {
        String hint = dictionary.getHint();
        String result = dictionary.compareAssuming(hint, answer);
        dictionary.addConstraint(hint, result);
        steps--;
        return hint;
    }

    public boolean isStepsLeft() {
        return steps > 0;
    }

    public boolean isWin() {
        return isWin;
    }

    public void clearConstraints() {
        dictionary.clearConstraints();
    }
}
