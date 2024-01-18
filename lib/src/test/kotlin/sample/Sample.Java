package sample;

import io.github.eendroroy.kotp.Algorithm;
import io.github.eendroroy.kotp.HOTP;
import io.github.eendroroy.kotp.TOTP;
import io.github.eendroroy.kotp.config.HOTPConfig;
import io.github.eendroroy.kotp.config.TOTPConfig;

import java.lang.RuntimeException;
import java.util.Calendar;

/**
 * @author indrajit
 */
public class Sample {
    public static void main(String... args) {
        String secret = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        hotpDemo(secret, 6, Algorithm.SHA1, 10);
        hotpDemo(secret, 8, Algorithm.SHA1, 10);
        hotpDemo(secret, 8, Algorithm.SHA256, 10);
        hotpDemo(secret, 8, Algorithm.SHA512, 10);
        hotpDemo(secret, 8, Algorithm.SHA1, 16);
        hotpDemo(secret, 8, Algorithm.SHA256, 16);
        hotpDemo(secret, 8, Algorithm.SHA512, 16);
        hotpDemo(secret, 8, Algorithm.SHA1, 36);
        hotpDemo(secret, 8, Algorithm.SHA256, 36);
        hotpDemo(secret, 8, Algorithm.SHA512, 36);
        totpDemo(secret, 6, 30, Algorithm.SHA1, 10);
        totpDemo(secret, 8, 30, Algorithm.SHA1, 10);
        totpDemo(secret, 8, 30, Algorithm.SHA256, 10);
        totpDemo(secret, 8, 30, Algorithm.SHA512, 10);
        totpDemo(secret, 8, 30, Algorithm.SHA1, 16);
        totpDemo(secret, 8, 30, Algorithm.SHA256, 16);
        totpDemo(secret, 8, 30, Algorithm.SHA512, 16);
        totpDemo(secret, 8, 30, Algorithm.SHA1, 36);
        totpDemo(secret, 8, 30, Algorithm.SHA256, 36);
        totpDemo(secret, 8, 30, Algorithm.SHA512, 36);
    }

    static void hotpDemo(String secret, Integer digits, Algorithm algorithm, Integer radix) {
        HOTPConfig config = new HOTPConfig(secret, digits, algorithm, radix);
        HOTP hotp = new HOTP(config);
        long counter = System.currentTimeMillis() / 1_000;

        String otp = hotp.at(counter);
        Long verify = hotp.verify(otp, counter);

        System.out.println();
        System.out.println();
        try {
            System.out.println(hotp.provisioningUri("KOTP_HOTP"));
        } catch (RuntimeException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println(digits + " <> " + algorithm + " <> " + radix);
        System.out.println(counter + "  ==>  " + otp + "  <>  + " + (counter == verify));
    }

    static void totpDemo(String secret, Integer length, Integer interval, Algorithm algorithm, Integer radix) {
        TOTPConfig config = new TOTPConfig(secret, "kotp_lib", length, interval, algorithm, radix);
        TOTP totp = new TOTP(config);
        long seconds = Calendar.getInstance().getTime().getTime() / 1_000;

        String otp = totp.at(seconds);
        Long verify = totp.verify(otp, seconds);

        String otpNow = totp.now();
        Long verifyNow = totp.verify(otpNow);

        System.out.println();
        System.out.println();
        try {
            System.out.println(totp.provisioningUri("KOTP_TOTP"));
        } catch (RuntimeException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println(length + " <> " + algorithm + " <> " + radix);
        System.out.println(seconds + "  ==>  " + otp + "  <>  " + (verify != null));
        System.out.println("NOW         ==>  " + otpNow + "  <>  " + (verifyNow != null));
    }
}