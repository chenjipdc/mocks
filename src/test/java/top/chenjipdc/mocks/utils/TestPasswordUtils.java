package top.chenjipdc.mocks.utils;

import org.junit.Test;

public class TestPasswordUtils {

    @Test
    public void lowerLetter() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.lowerLetter(20));
        }
    }

    @Test
    public void upperLetter() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.upperLetter(20));
        }
    }

    @Test
    public void number() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.number(20));
        }
    }

    @Test
    public void letter() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.letter(20));
        }
    }

    @Test
    public void letterNumber() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.letterNumber(20));
        }
    }

    @Test
    public void random() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordUtils.random(20));
        }
    }
}
