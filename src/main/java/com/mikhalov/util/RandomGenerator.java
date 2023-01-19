package com.mikhalov.util;

import java.util.Random;

public class RandomGenerator {

    private static final Random random = new Random();

    public static String getRandomString() {
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(4, 10);
        String str = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static int getRandomAge() {
        return random.nextInt(10, 91);
    }

    public static int getRandomIntForOrder() {
        return random.nextInt(1, 6);
    }
}
