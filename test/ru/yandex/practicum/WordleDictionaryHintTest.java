package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.NoHintsAvailableException;

import java.util.List;

public class WordleDictionaryHintTest {

    @Test
    void hintIsFromDictionary() {
        WordleDictionary dict = new WordleDictionary(List.of(
                "качок", "кабан", "катер", "гранд", "кудла"
        ));
        String result = dict.compareAssuming("гранд", "качок");
        dict.addConstraint("гранд", result);

        String hint = dict.getHint();
        Assertions.assertTrue(dict.isDictionaryContain(hint));
    }

    @Test
    void noHintsWhenAllUsed() {
        WordleDictionary dict = new WordleDictionary(List.of("качок"));

        // единственное слово уже использовано
        dict.addConstraint("качок", "+++++");

        Assertions.assertThrows(NoHintsAvailableException.class, dict::getHint);
    }

    @Test
    void constraintsDoNotContradictAnswer() {
        // ключевой тест: после реальных попыток ответ должен проходить ограничения
        WordleDictionary dict = new WordleDictionary(List.of(
                "качок", "кабан", "катер", "какао", "кагор",
                "гранд", "кудла", "кагат", "камыш", "карат"
        ));
        String answer = "качок";

        String[] guesses = {"гранд", "кудла", "кагат"};
        for (String guess : guesses) {
            String result = dict.compareAssuming(guess, answer);
            dict.addConstraint(guess, result);
        }

        // подсказка должна существовать — иначе ограничения сломаны
        Assertions.assertDoesNotThrow(dict::getHint);
    }
}
