package rs.kunpero.optional;

import org.junit.Test;

import java.util.function.Predicate;

import static rs.kunpero.optional.util.LambdaUtils.isPalindrome;
import static rs.kunpero.optional.util.LambdaUtils.isPrime;

public class LambdaUtilsTest {
    private static Predicate<Long> isOddSimple = n -> n % 2 == 0;

    private static Predicate<Long> isPrimeSimple = n -> {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    };

    private static Predicate<Long> isPalindromeSimple = n -> {
        if (n < 0) {
            return false;
        }
        long palindrome = n;
        long reverse = 0;

        while (palindrome != 0) {
            long remainder = palindrome % 10;
            reverse = reverse * 10 + remainder;
            palindrome = palindrome / 10;
        }

        return n == reverse;

    };

    @Test
    public void isOddTest() {
        for (long i = -100; i < 100; i++) {
            assert isOddSimple.test(i) == isOddSimple.test(i);
        }
    }

    @Test
    public void isPrimeTest() {
        for (long i = -100; i < 100; i++) {
            assert isPrime.test(i) == isPrimeSimple.test(i);
        }
    }

    @Test
    public void isPalindromeTest() {
        for (long i = -10000; i < 10000; i++) {
            assert isPalindrome.test(i) == isPalindromeSimple.test(i);
        }
    }
}
