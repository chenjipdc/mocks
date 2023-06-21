package top.chenjipdc.mocks.utils;

import org.junit.Test;

public class TestUtils {

    @Test
    public void testBool() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextBool());
        }
    }

    @Test
    public void testShort() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextShort());
        }
    }

    @Test
    public void testInt() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextInt(2));
        }
    }

    @Test
    public void testLong() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextLong());
        }
    }

    @Test
    public void testFloat() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextFloat());
        }
    }

    @Test
    public void testDouble() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextDouble());
        }
    }

    @Test
    public void testPort() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NumericUtils.nextPort(30000, 33000));
        }
    }

    @Test
    public void testIp() {
        for (int i = 0; i < 100; i++) {
            System.out.println(IpUtils.ipv4Lan());
        }
    }

    @Test
    public void testGeo() {
        for (int i = 0; i < 100; i++) {
            System.out.println(GeoUtils.random(1));
        }
    }

    @Test
    public void testDate() {
        for (int i = 0; i < 100; i++) {
            System.out.println(DateUtils.randomStringRangeDate("2023-06-01 00:00:00", "2023-06-30 00:00:00"));
        }
    }
}
