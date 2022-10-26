package net.nextbattle.quarry.functions;

import java.math.BigInteger;
import java.security.SecureRandom;

public class StringFunctions {
    private static SecureRandom random = new SecureRandom();
    public static String generateRandomID() {
        return new BigInteger(130, random).toString(32);
    }
}
