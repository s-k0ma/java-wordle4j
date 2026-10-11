package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Класс для работы с условиями для подбора подсказок
 */
public class WordleConstraints {
    private final List<Predicate<String>> constraints;

    public WordleConstraints() {
        this.constraints = new ArrayList<>();
    }

    public void addConstraint(Predicate<String> constraint) {
        this.constraints.add(constraint);
    }

    public boolean matches(String word) {
        return constraints.stream().allMatch(pr -> pr.test(word));
    }

    public void clearConstraints() {
        this.constraints.clear();
    }
}
