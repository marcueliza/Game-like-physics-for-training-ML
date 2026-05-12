package ro.tuiasi.edu.Proiect_PIP;

import junit.framework.TestCase;
import java.io.File;

public class AppTest extends TestCase {

    /**
     * Verifică dacă instanța Simulare se creează corect 
     * și dacă fișierul CSV este generat la inițializare.
     */
    public void testSimulareInitialization() {
        Simulare simulare = new Simulare();
        assertNotNull("Obiectul simulare nu ar trebui să fie null", simulare);
        
        File csvFile = new File("dataset.csv");
        assertTrue("Fișierul dataset.csv ar trebui să fie creat", csvFile.exists());
    }

    /**
     * Verifică dacă dimensiunile ferestrei din App sunt setate (indirect).
     */
    public void testAppFrameContent() {
        // Testăm dacă putem crea fereastra fără erori
        try {
            App.main(new String[]{});
            assertTrue(true);
        } catch (Exception e) {
            fail("Aplicația a crăpat la pornire: " + e.getMessage());
        }
    }
}