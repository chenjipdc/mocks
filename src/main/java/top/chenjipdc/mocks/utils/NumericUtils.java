package top.chenjipdc.mocks.utils;

import top.chenjipdc.mocks.other.Validate;

import java.util.Random;


/**
 * @author chenjipdc@gmail.com
 * @date 2022/3/31 4:21 下午
 */
public class NumericUtils {

    private static final Random RANDOM = new Random();

    // -------------------  bool   -------------------

    public static boolean nextBool() {
        return RANDOM.nextBoolean();
    }

    // -------------------  byte   -------------------

    public static byte[] nextBytes(int count) {
        Validate.isTrue(count < 1, "count必须大于0");
        final byte[] result = new byte[count];
        RANDOM.nextBytes(result);
        return result;
    }

    // -------------------  short   -------------------

    public static short nextShort() {
        return nextShort(Short.MAX_VALUE);
    }

    public static short nextShort(short bound) {
        return nextShort((short) 0,
                bound);
    }

    public static short nextShort(short start, short end) {
        return (short) nextInt(start,
                end);
    }

    // -------------------  int   -------------------

    public static int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    public static int nextInt(int bound) {
        return nextInt(0,
                bound);
    }

    public static int nextInt(int start, int end) {
        Validate.isTrue(start <= end,
                "start不能小于end");
        return start + RANDOM.nextInt(end - start);
    }

    // -------------------  long   -------------------

    public static long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    public static long nextLong(final long start, final long end) {
        Validate.isTrue(start <= end,
                "start不能小于end");
        return start + nextLong(end - start);
    }

    public static long nextLong(final long n) {
        long bits;
        long val;
        do {
            bits = RANDOM.nextLong() >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0);

        return val;
    }


    // -------------------  float   -------------------

    public static float nextFloat() {
        return nextFloat(Float.MAX_VALUE);
    }

    public static float nextFloat(float bound) {
        return nextFloat(0,
                bound);
    }

    public static float nextFloat(final float start, final float end) {
        Validate.isTrue(start <= end,
                "start不能小于end");
        return start + ((end - start) * RANDOM.nextFloat());
    }

    // -------------------  double   -------------------

    public static double nextDouble() {
        return nextDouble(Double.MAX_VALUE);
    }

    public static double nextDouble(double bound) {
        return nextDouble(0,
                bound);
    }

    public static double nextDouble(double start, double end) {
        Validate.isTrue(start <= end,
                "start不能小于end");
        return start + ((end - start) * RANDOM.nextDouble());
    }

    // -------------------  port   -------------------

    public static final int PORT = 65535;

    public static int nextPort() {
        return nextPort(PORT);
    }

    public static int nextPort(int bound) {
        return nextPort(0,
                bound);
    }

    public static int nextPort(int start, int end) {
        Validate.isTrue(start <= end,
                "start不能小于end");
        Validate.isTrue(end <= PORT && start >= 0,
                "端口范围0-65535");
        return nextInt(start,
                end);
    }


}
