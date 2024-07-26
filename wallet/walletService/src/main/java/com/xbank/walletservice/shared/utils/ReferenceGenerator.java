package com.xbank.walletservice.shared.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class ReferenceGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int REFERENCE_LENGTH = 24;
    private static final AtomicLong counter = new AtomicLong(0);
    private static final long COUNTER_MAX = 999999L;

    public static synchronized String generateUniqueReference() {
        long timestamp = Instant.now().toEpochMilli() % 100000000L;

        long counterValue = counter.getAndIncrement();
        if (counterValue > COUNTER_MAX) {
            counter.set(0);
            counterValue = 0;
        }

        String base36Timestamp = Long.toString(timestamp, 36).toUpperCase();
        String base36Counter = Long.toString(counterValue, 36).toUpperCase();

        int randomLength = REFERENCE_LENGTH - base36Timestamp.length() - base36Counter.length();
        String randomComponent = generateRandomString(randomLength);

        return base36Timestamp + base36Counter + randomComponent;
    }

    private static String generateRandomString(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return randomString.toString();
    }
}
