package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Simulare extends JPanel {

	// parametrii
	private double g = 1.0; // gravitatie
	private double m1 = 10.0, m2 = 10.0; // mase
	private double l1 = 150.0, l2 = 150.0; // lungimi bratep
	private long startTime;

	// unghiuri si viteze unghiulare
	private double a1 = Math.PI / 2, a2 = Math.PI / 2;
	private double a1_v = 0, a2_v = 0;

	private File csvFile;

	public Simulare() {
		csvFile = new File("dataset.csv");
		// csv
		// theta1 = unghiul primului (rad)
		// theta2= unghiul al doilea brat (rad)
		// viteza1 = viteza unghiulara brat 1
		// viteza2 = viteza unghiulara brat 2
		startTime = System.currentTimeMillis();

		try (FileWriter writer = new FileWriter(csvFile)) {
			writer.write("timp,theta1,theta2,v1,v2,x1,,x2,y1,y2\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer timer = new Timer(8, e -> { // 8ms update timer 125 hz
			updatePhysics();
			repaint();
		});
		timer.start();
	}

	private void updatePhysics() {
		// ecuatii dif pt acceleratii
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

		// locul de unde porneste
		double x0 = 600;
		double y0 = 200;

		double x1 = x0 + l1 * Math.sin(a1);
		double y1 = y0 + l1 * Math.cos(a1);
		double x2 = x1 + l2 * Math.sin(a2);
		double y2 = y1 + l2 * Math.cos(a2);

		saveToCSV(a1, a2, a1_v, a2_v, x1, y1, x2, y2);
	}

	private void saveToCSV(double t1, double t2, double v1, double v2, double x1, double y1, double x2, double y2) {
		try (FileWriter writer = new FileWriter(csvFile, true)) {
			double timpSimulare = (System.currentTimeMillis() - startTime) / 1000.0; // transformare in secunde
			writer.write(timpSimulare + "," + t1 + "," + t2 + "," + v1 + "," + v2 + "," + x1 + "," + y1 + "," + x2 + ","
					+ y2 + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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