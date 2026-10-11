package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.NoHintsAvailableException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для хранения игрового словаря
 * Сравнивает введенные слова,
 * Возвращает подсказки и загаданное слво,
 * Хранит результаты совпадний прошлых попыток
 */
public class WordleDictionary {

    private static final String NO_HINT_FOUND_MSG = "Подходящих подсказок не найдено";

    private final List<String> words;
    private final WordleConstraints constraints;
    private final Random random;
    private final Set<String> usedWords;

    public WordleDictionary(List<String> words) {
        this.words = words;
        this.constraints = new WordleConstraints();
        this.random = new Random();
        this.usedWords = new HashSet<>();
    }

    public boolean isDictionaryContain(String word) {
        return words.contains(word);
    }

    /**
     * Метод вызывается в начале игры
     * Определяет слово для отгадывания игроком
     *
     * @return случайное слово из словаря
     */
    public String pickAnswer() {
        return words.get(random.nextInt(words.size()));
    }

    /**
     * Метод сопоставляет ввод пользователя с правильным ответом
     *
     * @param assuming - слово введенное пользователем
     * @return - символьное представление совпадений букв
     * '+' — буква на своём месте,
     * '^' — буква есть, но не на этом месте,
     * '-' — буквы нет в слове
     */
    public String compareAssuming(String assuming, String answer) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char ch : answer.toCharArray())
            counts.merge(ch, 1, Integer::sum);

        StringBuilder sb = new StringBuilder(assuming.length());
        for (int i = 0; i < assuming.length(); i++) {
            if (answer.charAt(i) == assuming.charAt(i)) {
                sb.append('+');
                counts.merge(assuming.charAt(i), -1, Integer::sum);
            } else sb.append('.');
        }

        for (int i = 0; i < assuming.length(); i++) {
            if (sb.charAt(i) == '+') continue;
            int remains = counts.getOrDefault(assuming.charAt(i), 0);
            if (remains > 0) {
                sb.setCharAt(i, '^');
                counts.merge(assuming.charAt(i), -1, Integer::sum);
            } else {
                sb.setCharAt(i, '-');
            }
        }
        return sb.toString();
    }

    /**
     * После предложенного слова на основе результатов добавляется условие для выбора подсказок
     * {@link WordleConstraints}
     *
     * @param word        - предложенное слово
     * @param matchResult - символьное представление совпадений букв
     */
    public void addConstraint(String word, String matchResult) {
        usedWords.add(word);
        Set<Character> present = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            if (matchResult.charAt(i) == '+' || matchResult.charAt(i) == '^')
                present.add(word.charAt(i));
        }

        for (int i = 0; i < matchResult.length(); i++) {
            int position = i;
            char ch = word.charAt(position);
            switch (matchResult.charAt(i)) {
                case '+':
                    constraints.addConstraint(str -> str.charAt(position) == ch);
                    break;
                case '^':
                    constraints.addConstraint(str -> str.indexOf(ch) >= 0 && str.charAt(position) != ch);
                    break;
                case '-':
                    if (!present.contains(word.charAt(position)))
                        constraints.addConstraint(str -> str.indexOf(ch) < 0);
                    break;

            }
        }
    }

    /**
     * Отчищает набор условий в конце игрового цикла
     */
    public void clearConstraints() {
        usedWords.clear();
        constraints.clearConstraints();
    }

    /**
     * Возвращает слово-подсказку
     * Учитываются результаты и совпадения всех прошлых попыткок и подсказкок
     * Добавляет ограничение в WordleConstraints
     * для избежания повторной выдачи одной и той же подсказки
     */
    public String getHint() {
        List<String> hints = words.stream()
                .filter(constraints::matches)
                .filter(str -> !usedWords.contains(str))
                .collect(Collectors.toList());

        if (hints.isEmpty()) throw new NoHintsAvailableException(NO_HINT_FOUND_MSG);

        return hints.get(random.nextInt(hints.size()));
    }
}
