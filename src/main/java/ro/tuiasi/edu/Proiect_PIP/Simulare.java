package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Simulare extends JPanel {
    // Parametrii fizici
    private double g = 1.0; // gravitatie
    private double m1 = 10.0, m2 = 10.0; // Mase
    private double l1 = 150.0, l2 = 150.0; // Lungimi brate
    
    // Starea sistemului: unghiuri (radiani) și viteze unghiulare
    private double a1 = Math.PI / 2, a2 = Math.PI / 2;
    private double a1_v = 0, a2_v = 0;
    
    private File csvFile;

    public Simulare() {
        csvFile = new File("physics_dataset.csv");
        // Scriem header-ul CSV-ului
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("time,theta1,theta2,vel1,vel2,x2,y2\n");
        } catch (IOException e) { e.printStackTrace(); }

        Timer timer = new Timer(16, e -> {
            updatePhysics();
            repaint();
        });
        timer.start();
    }

    private void updatePhysics() {
        // Ecuațiile diferențiale pentru accelerații (Simplificate pentru implementare)
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

        // Integrare numerică
        a1_v += a1_a;
        a2_v += a2_a;
        a1 += a1_v;
        a2 += a2_v;

        // Calculăm poziția finală pentru vizualizare și dataset
        double x2 = 400 + l1 * Math.sin(a1) + l2 * Math.sin(a2);
        double y2 = 200 + l1 * Math.cos(a1) + l2 * Math.cos(a2);

        saveToCSV(a1, a2, a1_v, a2_v, x2, y2);
    }

    private void saveToCSV(double t1, double t2, double v1, double v2, double x, double y) {
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(System.currentTimeMillis() + "," + t1 + "," + t2 + "," + v1 + "," + v2 + "," + x + "," + y + "\n");
        } catch (IOException e) { e.printStackTrace(); }
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