package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

/**
 * Clasa {@code Simulare} implementează o simulare fizică în timp real a unui
 * pendul dublu, utilizând un model bazat pe ecuații diferențiale neliniare.
 *
 * <h2>Scop</h2>
 * <p>
 * Această clasă face parte dintr-o platformă pentru generarea de date sintetice
 * destinate antrenării modelelor de Machine Learning.
 * </p>
 *
 * <h2>Funcționalități</h2>
 * <ul>
 * <li>Simulare fizică a unui pendul dublu</li>
 * <li>Randare grafică în timp real folosind Swing</li>
 * <li>Export automat al datelor în format CSV</li>
 * </ul>
 *
 * <h2>Date generate</h2>
 * <ul>
 * <li>Timpul simulat (secunde)</li>
 * <li>Unghiurile pendulelor (radiani)</li>
 * <li>Vitezele unghiulare</li>
 * <li>Pozițiile carteziene ale maselor</li>
 * </ul>
 *
 * <h2>Model fizic</h2>
 * <p>
 * Sistemul este un pendul dublu ideal, fără frecare. Integrarea numerică este
 * realizată folosind metoda Euler.
 * </p>
 *
 * <h2>Format CSV</h2>
 * 
 * <pre>
 * timp,theta1,theta2,v1,v2,x1,y1,x2,y2
 * </pre>
 *
 * <h2>Limitări</h2>
 * <ul>
 * <li>Metoda Euler introduce erori numerice în timp</li>
 * <li>Nu există conservare exactă a energiei</li>
 * <li>Scrierea frecventă în fișier poate afecta performanța</li>
 * </ul>
 *
 * <h2>Extensibilitate</h2>
 * <ul>
 * <li>Suport pentru alte sisteme fizice</li>
 * <li>Export în alte formate (JSON, baze de date)</li>
 * <li>Integrare cu API-uri externe</li>
 * </ul>
 *
 * @version 1.0
 */
public class Simulare extends JPanel {

	/**
	 * Parametrii fizici și starea curentă a sistemului.
	 */

	/**
	 * Accelerația gravitațională utilizată în simulare. Unitate: scalată (nu
	 * reprezintă m/s² real).
	 */
	private double g = 1.0;

	/**
	 * Masele celor două corpuri ale pendulului. Unitate: arbitrară (folosită pentru
	 * stabilitatea simulării).
	 */
	private double m1 = 10.0, m2 = 10.0;

	/**
	 * Lungimile brațelor pendulului. Unitate: pixeli (folosite pentru randare
	 * grafică).
	 */
	private double l1 = 150.0, l2 = 150.0;

	/**
	 * Momentul de start al simulării. Unitate: milisecunde
	 * (System.currentTimeMillis).
	 */
	private long startTime;

	/**
	 * Unghiurile curente ale pendulelor. Unitate: radiani.
	 */
	private double a1 = Math.PI / 2, a2 = Math.PI / 2;

	/**
	 * Vitezele unghiulare ale pendulelor. Unitate: radiani / frame (integrare
	 * Euler).
	 */
	private double a1_v = 0, a2_v = 0;

	/**
	 * Fișierul utilizat pentru stocarea datasetului generat. Format: CSV.
	 */
	private File csvFile;

	/**
	 * Inițializează simularea pendulului dublu.
	 *
	 * <p>
	 * Creează fișierul CSV și scrie header-ul datasetului. Pornește bucla de
	 * simulare folosind {@link javax.swing.Timer}.
	 * </p>
	 *
	 * <p>
	 * Frecvența de actualizare este aproximativ 125 Hz (8 ms).
	 * </p>
	 */
	public Simulare() {
		csvFile = new File("dataset.csv");
		startTime = System.currentTimeMillis();

		try (FileWriter writer = new FileWriter(csvFile)) {
			writer.write("timp,theta1,theta2,v1,v2,x1,x2,y1,y2\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer timer = new Timer(8, e -> { // 8ms update timer 125 hz
			updatePhysics();
			repaint();
		});
		timer.start();
	}

	/**
	 * Actualizează starea fizică a sistemului.
	 *
	 * <p>
	 * Calculează accelerațiile unghiulare folosind ecuațiile pendulului dublu și
	 * aplică integrarea numerică (Euler).
	 * </p>
	 *
	 * <p>
	 * Determină pozițiile carteziene ale maselor și trimite rezultatele către
	 * metoda de salvare.
	 * </p>
	 */
	private void updatePhysics() {
		/**
		 * Acestea sunt ecuatiile diferentiale pentru pendul.
		 */
		double num1 = -g * (2 * m1 + m2) * Math.sin(a1);
		double num2 = -m2 * g * Math.sin(a1 - 2 * a2);
		double num3 = -2 * Math.sin(a1 - a2) * m2;
		double num4 = a2_v * a2_v * l2 + a1_v * a1_v * l1 * Math.cos(a1 - a2);
		double den = l1 * (2 * m1 + m2 - m2 * Math.cos(2 * a1 - 2 * a2));
		double a1_a = (num1 + num2 + num3 * num4) / den;

		num1 = 2 * Math.sin(a1 - a2);
		num2 = (a1_v * a1_v * l1 * (m1 + m2));
		num3 = g * (m1 + m2) * Math.cos(a1);
		num4 = a2_v * a2_v * l2 * m2 * Math.cos(a1 - a2);
		den = l2 * (2 * m1 + m2 - m2 * Math.cos(2 * a1 - 2 * a2));
		double a2_a = num1 * (num2 + num3 + num4) / den;

		a1_v += a1_a;
		a2_v += a2_a;
		a1 += a1_v;
		a2 += a2_v;

		double x0 = 600;
		double y0 = 200;

		double x1 = x0 + l1 * Math.sin(a1);
		double y1 = y0 + l1 * Math.cos(a1);
		double x2 = x1 + l2 * Math.sin(a2);
		double y2 = y1 + l2 * Math.cos(a2);

		saveToCSV(a1, a2, a1_v, a2_v, x1, y1, x2, y2);
	}

	/**
	 * Salvează starea curentă a simulării în fișierul CSV.
	 *
	 * <p>
	 * Fiecare apel adaugă o linie nouă în dataset.
	 * </p>
	 *
	 * @param t1 unghiul primului pendul (radiani)
	 * @param t2 unghiul celui de-al doilea pendul (radiani)
	 * @param v1 viteza unghiulară a primului pendul
	 * @param v2 viteza unghiulară a celui de-al doilea pendul
	 * @param x1 coordonata X a primului punct
	 * @param y1 coordonata Y a primului punct
	 * @param x2 coordonata X a celui de-al doilea punct
	 * @param y2 coordonata Y a celui de-al doilea punct
	 */
	private void saveToCSV(double t1, double t2, double v1, double v2, double x1, double y1, double x2, double y2) {
		try (FileWriter writer = new FileWriter(csvFile, true)) {
			double timpSimulare = (System.currentTimeMillis() - startTime) / 1000.0; // transformare in secunde
			writer.write(timpSimulare + "," + t1 + "," + t2 + "," + v1 + "," + v2 + "," + x1 + "," + y1 + "," + x2 + ","
					+ y2 + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Desenează pendulul pe ecran.
	 *
	 * <p>
	 * Metoda este apelată automat de Swing la fiecare repaint. Redă poziția curentă
	 * a celor două mase și a brațelor.
	 * </p>
	 *
	 * @param g2 contextul grafic furnizat de Swing
	 */
	@Override
	protected void paintComponent(Graphics g2) {

		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;
		g.setStroke(new BasicStroke(2));

		int x0 = 400, y0 = 200;
		int x1 = (int) (x0 + l1 * Math.sin(a1));
		int y1 = (int) (y0 + l1 * Math.cos(a1));
		int x2 = (int) (x1 + l2 * Math.sin(a2));
		int y2 = (int) (y1 + l2 * Math.cos(a2));

		g.drawLine(x0, y0, x1, y1);
		g.fillOval(x1 - 10, y1 - 10, 20, 20);
		g.drawLine(x1, y1, x2, y2);
		g.fillOval(x2 - 10, y2 - 10, 20, 20);
	}

}