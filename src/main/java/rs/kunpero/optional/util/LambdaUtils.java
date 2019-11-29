package rs.kunpero.optional.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class LambdaUtils {
    public static Predicate<Long> isOdd = n -> (n & 1) != 0;

    public static Predicate<Long> isPrime = n -> {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (!isOdd.test(n)|| n % 3 == 0) return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for (long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
        }
        return true;
    };

    public static Predicate<Long> isPalindrome = n -> {
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            return true;
        }

        List<Byte> digits = new ArrayList<>();
        while (n > 0) {
            digits.add((byte) (n % 10));
            n /= 10;
        }
        return IntStream.range(0, digits.size() / 2)
                .allMatch(index -> digits.get(index) == digits.get(digits.size() - 1 - index));

    };
}
