package top.chenjipdc.mocks.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordUtils {

    public static final String[] LOWER_LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static final String[] UPPER_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static String[] LETTERS;

    static {
        List<String> letter = new ArrayList<>(Arrays.asList(LOWER_LETTERS));
        letter.addAll(Arrays.asList(UPPER_LETTERS));
        LETTERS = letter.toArray(new String[0]);
    }

    public static final String[] NUMBER = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static String[] LETTERS_NUMBER;

    static {
        List<String> letter = new ArrayList<>(Arrays.asList(LETTERS));
        letter.addAll(Arrays.asList(NUMBER));
        LETTERS_NUMBER = letter.toArray(new String[0]);
    }

    public static final String[] SPEC = {"~", "`", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "[", "{", "]", "}", "|", ";", ":", "'", ",", "<", ".", ">", "/", "?"};
    public static String[] ALL;

    static {
        List<String> letter = new ArrayList<>(Arrays.asList(LETTERS_NUMBER));
        letter.addAll(Arrays.asList(SPEC));
        ALL = letter.toArray(new String[0]);
    }

    public static String random(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALL[NumericUtils.nextInt(ALL.length)]);
        }
        return sb.toString();
    }

    public static String lowerLetter(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(LOWER_LETTERS[NumericUtils.nextInt(LOWER_LETTERS.length)]);
        }
        return sb.toString();
    }

    public static String upperLetter(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(UPPER_LETTERS[NumericUtils.nextInt(UPPER_LETTERS.length)]);
        }
        return sb.toString();
    }

    public static String letter(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERS[NumericUtils.nextInt(LETTERS.length)]);
        }
        return sb.toString();
    }

    public static String number(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER[NumericUtils.nextInt(NUMBER.length)]);
        }
        return sb.toString();
    }


    public static String letterNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERS_NUMBER[NumericUtils.nextInt(LETTERS_NUMBER.length)]);
        }
        return sb.toString();
    }
}
