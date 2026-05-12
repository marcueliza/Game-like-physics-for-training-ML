package ro.tuiasi.edu.Proiect_PIP;

import junit.framework.TestCase;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimulareTest extends TestCase {

	private Simulare simulare;
	private final String CSV_FILE_NAME = "dataset.csv";

	protected void setUp() throws Exception {
		super.setUp();
		simulare = new Simulare();
	}

	/**
	 * Testează dacă fișierul CSV este creat la inițializarea obiectului.
	 */
	public void testCsvFileCreation() {
		File file = new File(CSV_FILE_NAME);
		assertTrue("Fișierul CSV ar trebui să existe după inițializare", file.exists());
	}

	/**
	 * Testează dacă header-ul CSV-ului este scris corect.
	 */
	public void testCsvHeaderContent() throws IOException {
		File file = new File(CSV_FILE_NAME);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String header = reader.readLine();
		reader.close();

		assertNotNull("Header-ul nu ar trebui să fie null", header);
		assertTrue("Header-ul ar trebui să conțină coloana 'timp'", header.contains("timp"));
		assertTrue("Header-ul ar trebui să conțină coloana 'theta1'", header.contains("theta1"));
	}

	/**
	 * Testează dacă simularea este o componentă validă de tip JPanel.
	 */
	public void testComponentProperties() {
		assertTrue("Simulare ar trebui să fie opacă", simulare.isOpaque());
		assertNotNull("Simulare ar trebui să aibă un layout manager (chiar dacă e default)", simulare.getLayout());
	}

	/**
	 * Testează dacă scrierea datelor funcționează. (Verifică dacă după o scurtă
	 * așteptare fișierul conține mai mult de o linie).
	 */
	public void testDataWritingPerformance() throws InterruptedException, IOException {
		// Așteptăm 100ms pentru ca Timer-ul să execute câteva iterații
		Thread.sleep(100);

		File file = new File(CSV_FILE_NAME);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lineCount = 0;
		while (reader.readLine() != null) {
			lineCount++;
		}
		reader.close();

		// Header + cel puțin o linie de date
		assertTrue("Ar trebui să avem cel puțin 2 linii în CSV", lineCount >= 2);
	}
}
