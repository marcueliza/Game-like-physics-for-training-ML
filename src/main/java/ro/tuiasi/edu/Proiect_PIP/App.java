package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.JFrame;

/**
 * Clasa de intrare în aplicație.
 *
 * <p>
 * Inițializează interfața grafică și pornește simularea pendulului dublu prin
 * instanțierea clasei {@code Simulare}.
 * </p>
 */
public class App {
	/**
	 * Punctul de intrare al aplicației.
	 *
	 * <p>
	 * Creează fereastra principală și atașează componenta de simulare.
	 * </p>
	 *
	 * @param args argumente din linia de comandă (neutilizate)
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame("Simulator pendul");
		frame.add(new Simulare());
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}