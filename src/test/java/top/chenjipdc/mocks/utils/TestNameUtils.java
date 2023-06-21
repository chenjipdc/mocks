package top.chenjipdc.mocks.utils;

import org.junit.Test;

public class TestNameUtils {

    @Test
    public void test() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(NameUtils.random());
        }
    }
}
