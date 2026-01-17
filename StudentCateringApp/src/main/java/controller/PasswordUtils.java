package controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

/**
 * Χρήσιμες μέθοδοι για ασφαλή διαχείριση κωδικών μέσω κρυπτογράφησης PBKDF2.
 */
public class PasswordUtils {

    // Πλήθος επαναλήψεων του αλγορίθμου - όσο μεγαλύτερο, τόσο ασφαλέστερο αλλά πιο αργό
    private static final int ITERATIONS = 10000;

    // Μήκος του παραγόμενου hash σε bits
    private static final int KEY_LENGTH = 256;

    /**
     * Δημιουργεί ένα ασφαλές hash του κωδικού, με χρήση PBKDF2 και τυχαίου salt.
     * Το αποτέλεσμα είναι συμβολοσειρά της μορφής: base64(salt)$base64(hash)
     */
    public static String hashPassword(String password) {
        try {
            // Δημιουργία τυχαίου salt
            byte[] salt = generateSalt();

            // Ορισμός παραμέτρων για PBKDF2
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Δημιουργία hash με βάση το password και το salt
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Επιστροφή του hash και του salt σε μορφή base64 και ενωμένα με '$'
            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            // Αν προκύψει πρόβλημα, ρίχνουμε runtime εξαίρεση
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    /**
     * Δημιουργεί έναν τυχαίο salt 16 bytes.
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[16];  // 16 bytes = 128 bits
        new java.security.SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Επαληθεύει έναν κωδικό που εισήγαγε ο χρήστης, συγκρίνοντάς τον με αποθηκευμένο hash.
     * 
     * @param enteredPassword Ο κωδικός που εισήχθη από τον χρήστη
     * @param storedHash Η τιμή που έχει αποθηκευτεί στη βάση (salt$hash)
     * @return true αν ο κωδικός είναι σωστός, false διαφορετικά
     */
    public static boolean verifyPassword(String enteredPassword, String storedHash) {
        try {
            // Διαχωρισμός salt και hash από το αποθηκευμένο string
            String[] parts = storedHash.split("\\$");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedPasswordHash = Base64.getDecoder().decode(parts[1]);

            // Υπολογισμός hash για το password που δόθηκε, με το ίδιο salt
            byte[] enteredPasswordHash = hashPasswordWithSalt(enteredPassword, salt);

            // Σύγκριση των δύο hash
            return java.util.Arrays.equals(storedPasswordHash, enteredPasswordHash);

        } catch (Exception e) {
            throw new RuntimeException("Error while verifying password", e);
        }
    }

    /**
     * Υπολογίζει το hash ενός κωδικού με συγκεκριμένο salt, με χρήση PBKDF2.
     */
    private static byte[] hashPasswordWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing password with salt", e);
        }
    }
}
