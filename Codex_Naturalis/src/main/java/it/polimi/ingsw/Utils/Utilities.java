package it.polimi.ingsw.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utilities {
    public static String RandomStringGenerator(int length){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String StackTraceToString(Exception e){
        List<StackTraceElement> stackTrace = Arrays.stream(e.getStackTrace()).toList();
        StringBuilder stackTraceString = new StringBuilder();

        for(StackTraceElement element : stackTrace)
            stackTraceString.append(element.toString()).append("\n");

        return stackTraceString.toString();
    }
}
