package com.xbank.walletservice.shared.utils;
import java.util.Random;
import java.util.stream.Collectors;

public class AccountNumberGenerator {
    public static String generate() {
        Random random = new Random();
        return random.ints(10, 0, 10) // Generate a stream of 10 random integers between 0 and 9
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }
}
