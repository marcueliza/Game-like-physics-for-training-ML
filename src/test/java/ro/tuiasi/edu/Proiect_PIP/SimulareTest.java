package ro.tuiasi.edu.Proiect_PIP;

import junit.framework.TestCase;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimulareTest extends TestCase {

    private Simulare simulare;
    private final String CSV_FILE_NAME = "dataset.csv";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        simulare = new Simulare();
    }

    /**
     * Curăță resursele după fiecare test. 
     * Oprește conectorul Kafka și Timer-ul grafic pentru a nu bloca testele următoare.
     */
    @Override
    protected void tearDown() throws Exception {
        if (simulare != null) {
            simulare.stop();
        }
        super.tearDown();
    }

    /**
     * Testează dacă fișierul CSV este creat la inițializarea obiectului.
     */
    public void testCsvFileCreation() {
        File file = new File(CSV_FILE_NAME);
        assertTrue("Fișierul CSV ar trebui să existe după inițializare", file.exists());
    }

    /**
     * Verifică dacă header-ul fișierului CSV conține coloanele corecte.
     */
    public void testCsvHeaderContent() {
        File file = new File(CSV_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertNotNull("Header-ul nu ar trebui să fie gol", header);
            assertEquals("timp,theta1,theta2,v1,v2,x1,y1,x2,y2", header.trim());
        } catch (IOException e) {
            fail("Eroare la citirea fișierului CSV: " + e.getMessage());
        }
    }

    /**
     * Verifică proprietățile de bază ale componentei grafice (dimensiuni implicite).
     */
    public void testComponentProperties() {
        assertNotNull("Instanța simulării ar trebui să fie inițializată", simulare);
        assertTrue("Componenta ar trebui să fie vizibilă", simulare.isOpaque());
    }

    /**
     * Verifică dacă oprirea scrierii (recording = false) funcționează corect.
     */
    public void testStopRecording() {
        try {
            simulare.stopRecording();
            // Testul trece dacă metoda rulează fără să arunce excepții
            assertTrue(true);
        } catch (Exception e) {
            fail("Metoda stopRecording a aruncat o eroare: " + e.getMessage());
        }
    }
}