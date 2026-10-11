package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class WordleDictionaryTest {
    private WordleDictionary dictionary(String... words) {
        return new WordleDictionary(List.of(words));
    }


    @Test
    void allLettersOnPlace() {
        WordleDictionary dict = dictionary("качок");
        Assertions.assertEquals("+++++", dict.compareAssuming("качок", "качок"));
    }

    @Test
    void noMatchingLetters() {
        WordleDictionary dict = dictionary("качок");
        Assertions.assertEquals("-----", dict.compareAssuming("питер", "качок"));
    }

    @Test
    void letterPresentWrongPosition() {
        WordleDictionary dict = dictionary("хомут");
        Assertions.assertEquals("---^-", dict.compareAssuming("герой", "хомут"));
    }

    @Test
    void oneGreenRestGray() {
        WordleDictionary dict = dictionary("хомут");
        Assertions.assertEquals("---+-", dict.compareAssuming("шуруп", "хомут"));
    }

    @Test
    void duplicateLetterInAnswer() {
        WordleDictionary dict = dictionary("колос");
        Assertions.assertEquals("++---", dict.compareAssuming("котик", "колос"));
    }

    @Test
    void duplicateLetterInGuess() {
        WordleDictionary dict = dictionary("качок");
        Assertions.assertEquals("+-^+-", dict.compareAssuming("кокос", "качок"));
    }

    @Test
    void allDuplicates() {
        WordleDictionary dict = dictionary("какао");
        Assertions.assertEquals("+++++", dict.compareAssuming("какао", "какао"));

        WordleDictionary dict2 = dictionary("какан");
        Assertions.assertEquals("++++-", dict2.compareAssuming("какао", "какан"));
    }
}
