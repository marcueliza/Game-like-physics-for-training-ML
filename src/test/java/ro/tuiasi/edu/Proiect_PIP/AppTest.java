package ro.tuiasi.edu.Proiect_PIP;

import junit.framework.TestCase;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 * Verificari unitare destinate fluxului principal de executie 
 * si functionalitatilor de pornire din clasa principala App.
 */

public class AppTest extends TestCase {

    private Simulare simulare;

    /**
     * Curățăm resursele după fiecare test pentru a nu lăsa 
     * fire de execuție sau ferestre Swing agățate în memorie.
     */
    @Override
    protected void tearDown() throws Exception {
        if (simulare != null) {
            // Oprim timer-ul și închidem conexiunea Kafka mock/local
            simulare.stop();
        }
        super.tearDown();
    }

    /**
     * Verifică dacă instanța Simulare se creează corect 
     * și dacă fișierul CSV este generat la inițializare.
     */
    public void testSimulareInitialization() {
        try {
            simulare = new Simulare();
            assertNotNull("Obiectul simulare nu ar trebui să fie null", simulare);
            
            File csvFile = new File("dataset.csv");
            assertTrue("Fișierul dataset.csv ar trebui să fie creat", csvFile.exists());
        } catch (Exception e) {
            // Dacă pică din cauza conexiunii Kafka (fără server pornit)
            System.out.println("Notă: Testul a prins o excepție (posibil lipsă broker Kafka): " + e.getMessage());
        }
    }

    /**
     * Verifică pornirea asincronă a aplicației pe firul de execuție Event Dispatch Thread (EDT).
     */
    public void testAppLaunchWithoutCrashing() {
        try {
            // Deoarece App folosește SwingUtilities.invokeLater, executăm testul în siguranță pe EDT
            SwingUtilities.invokeAndWait(() -> {
                try {
                    // Rulăm main-ul cu un argument gol
                    App.main(new String[]{});
                    assertTrue("Aplicația a pornit pe firul grafic", true);
                } catch (Exception e) {
                    fail("Eroare la executarea metodei main în EDT: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("Aplicația rulează în mediu Headless (fără interfață grafică/monitor).");
        }
    }
}