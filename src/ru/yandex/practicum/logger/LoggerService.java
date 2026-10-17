package ru.yandex.practicum.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Вспомогательный класс
 * Пишет лог о состоянии игры
 */
public class LoggerService {

    private static final String ACTION_TEMPLATE = "%s : [%s] : %s : %s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Path LOG_DIR = Paths.get("log").toAbsolutePath();
    private static final ZoneId ZONE = ZoneId.of("UTC");

    private final LogLevel level;

    public LoggerService(LogLevel level) {
        this.level = level;
    }

    public void info(Class<?> clazz, String message) {
        if (level.ordinal() <= LogLevel.INFO.ordinal()) {
            generateLog(LogLevel.INFO, clazz, message);
        }
    }

    public void error(Class<?> clazz, String message) {
        if (level.ordinal() <= LogLevel.ERROR.ordinal()) {
            generateLog(LogLevel.ERROR, clazz, message);
        }
    }

    private void generateLog(LogLevel level, Class<?> clazz, String message) {
        try {
            writeLog(level, clazz, message);
        } catch (IOException ex) {
            System.err.println("Can't write log file: " + ex.getMessage());
        }
    }

    private void writeLog(LogLevel level, Class<?> clazz, String message) throws IOException {
        Files.createDirectories(LOG_DIR);

        Path logFile = LOG_DIR.resolve(LocalDate.now(ZONE) + ".log");
        String entry = String.format(ACTION_TEMPLATE, DATE_TIME_FORMATTER.format(OffsetDateTime.now(ZONE)), level, clazz.getSimpleName(), message);

        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(entry);
            writer.newLine();
        }
    }
}