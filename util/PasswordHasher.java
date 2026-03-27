package util;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordHasher {
    private PasswordHasher() {
    }

    public static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
    public static boolean verify(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }


}
