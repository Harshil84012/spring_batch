package com.softvan.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class GenerateData {
    public static void main(String[] args) {
        String csvFilePath = "src/main/resources/csvFile.csv";
        int rowCount = 100000;
        try {
            FileWriter csvWriter = new FileWriter(csvFilePath);
            Random random = new Random();
            for (int i = 1; i <= rowCount; i++) {
                String colA = String.valueOf(i);
                String colB = generateRandomWords(random);
                String colC = String.valueOf(createRandomDate(2022,
                        2024));
//        String colC = generateRandomNumbers(random, 3);
// String  colD = generateRandomNumbers(random, 5);
                csvWriter.append(colA).append(",").append(colB).append(",").append(colC).append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateRandomWords(Random
                                                      random) {
        StringBuilder sb = new StringBuilder();
        for
        (int i = 0; i < 3; i++) {
            char c1 = (char)
                    (random.nextInt(26) + 'A');
            sb.append(c1);
        }
        return
                sb.toString();
    }

    public static int
    createRandomIntBetween(int start, int end) {
        return start
                + (int) Math.round(Math.random() * (end - start));
    }

    public static LocalDate createRandomDate(int startYear,
                                             int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year =
                createRandomIntBetween(startYear, endYear);
        return
                LocalDate.of(year, month, day);
    }

    private static String generateRandomNumbers(Random random, int numDigits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i
                <= numDigits; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}