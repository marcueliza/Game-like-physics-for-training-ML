package ro.tuiasi.edu.Proiect_PIP;

import junit.framework.TestCase;
import java.io.File;

/**
 * Componenta de testare operationala pentru HuggingFaceUploader.
 * Valideaza reactia sistemului la incarcarea fisierelor corecte, inexistente sau cu parametri nuli.
 */

public class HuggingFaceUploaderTest extends TestCase {

    /**
     * Verifică dacă uploader-ul aruncă o excepție atunci când fișierul sursă nu există local.
     */
    public void testUploadWithMissingFile() {
        String tokenFictiv = "hf_test_token_12345";
        String repoFictiv = "SilviuMih21/ML_dataset_test";
        String fisierInexistent = "non_existent_dataset_file.csv";

        File file = new File(fisierInexistent);
        assertFalse(file.exists());

        try {
            HuggingFaceUploader.upload(fisierInexistent, repoFictiv, tokenFictiv);
            fail("Ar fi trebuit să se arunce o excepție deoarece fișierul de trimis lipsește!");
        } catch (Exception e) {
            // Testul este SUCCES dacă se prinde eroarea de fișier lipsă
            assertNotNull("Mesajul de eroare ar trebui să conțină detalii", e.getMessage());
        }
    }

    /**
     * Verifică rezistența uploaderului la parametri de tip Null.
     * Transmiterea unei căi nule pentru fișier trebuie să declanșeze o excepție Java nativă
     * înainte ca procesul de rețea sau cmd.exe să fie pornit.
     */
    public void testUploadWithNullParameters() {
        try {
            // Pasarea unui parametru null va forța o eroare nativă în Java (ex: NullPointerException)
            // în metodele interne I/O din HuggingFaceUploader, înainte de a rula vreo comandă cmd
            HuggingFaceUploader.upload(null, "SilviuMih21/ML_dataset", "token_fals");
            fail("Ar fi trebuit să eșueze instantaneu din cauza parametrului null!");
        } catch (Exception e) {
            // Testul trece cu succes deoarece Java a blocat execuția corect
            System.out.println("S-a prins eroarea de validare Java: " + e.toString());
            assertTrue(true);
        }
    }
}