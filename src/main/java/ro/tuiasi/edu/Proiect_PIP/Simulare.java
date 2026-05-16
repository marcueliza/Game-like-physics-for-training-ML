package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

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
 * <li>Randare grafică în timp real folosinf Swing</li>
 * <li>Export automat al datelor în format CSV și Kafka</li>
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
 * @version 1.0
 */
public class Simulare extends JPanel {

    private static final long serialVersionUID = 1L;

    // Parametrii fizici
    private double g = 1.0; // gravitatie
    private double m1 = 10.0, m2 = 10.0; // mase
    private double l1 = 150.0, l2 = 150.0; // lungimi brate
    private long startTime;

    // Unghiuri si viteze unghiulare
    private double a1 = Math.PI / 2, a2 = Math.PI / 2;
    private double a1_v = 0, a2_v = 0;

    private File csvFile;
    private Timer timer;
    private boolean recording = true; 

    // Variabile pentru Kafka
    private KafkaProducer<String, String> producer;
    private static final String TOPIC = "pendul-date";

    /**
     * Inițializează simularea pendulului dublu și conexiunea Kafka.
     */
    public Simulare() {
        csvFile = new File("dataset.csv");
        startTime = System.currentTimeMillis();

        // Initializare Header CSV
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("timp,theta1,theta2,v1,v2,x1,y1,x2,y2\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- Configurare Kafka Producer ---
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); 
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
        // ----------------------------------

        timer = new Timer(8, e -> { // 8ms update timer (~125 hz)
            updatePhysics();
            repaint();
        });
        timer.start();
    }

    /**
     * Oprește simularea și închide conexiunea cu producătorul Kafka.
     */
    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            System.out.println("Simulare oprită.");
        }
        
        if (producer != null) {
            producer.close();
            System.out.println("Conexiune Kafka închisă.");
        }
    }

    /**
     * Oprește scrierea datelor, dar lasă pendulul să se miște vizual.
     */
    public void stopRecording() {
        this.recording = false;
        System.out.println("Salvarea datelor a fost oprită. Pendulul continuă să se miște.");
    }

    /**
     * Actualizează starea fizică a sistemului folosind ecuațiile pendulului dublu.
     */
    private void updatePhysics() {
        // Ecuatii diferentiale pentru acceleratii (Double Pendulum)
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

        // Calcul pozitii pentru desenare si logare (Ancorat la 400, 200)
        double x0 = 400; 
        double y0 = 200;

        double x1 = x0 + l1 * Math.sin(a1);
        double y1 = y0 + l1 * Math.cos(a1);

        double x2 = x1 + l2 * Math.sin(a2);
        double y2 = y1 + l2 * Math.cos(a2);

        if (recording) {
            saveToCSV(a1, a2, a1_v, a2_v, x1, y1, x2, y2);
            sendToKafka(a1, a2, a1_v, a2_v, x1, y1, x2, y2);
        }
    }

    /**
     * Salvează starea curentă a simulării în fișierul CSV.
     */
    private void saveToCSV(double t1, double t2, double v1, double v2, double x1, double y1, double x2, double y2) {
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            double timpSimulare = (System.currentTimeMillis() - startTime) / 1000.0;
            writer.write(timpSimulare + "," + t1 + "," + t2 + "," + v1 + "," + v2 + "," + x1 + "," + y1 + "," + x2 + "," + y2 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Trimite stream-ul de date în timp real către topicul de Apache Kafka.
     */
    private void sendToKafka(double t1, double t2, double v1, double v2, double x1, double y1, double x2, double y2) {
        double timpSimulare = (System.currentTimeMillis() - startTime) / 1000.0;
        String mesajDate = timpSimulare + "," + t1 + "," + t2 + "," + v1 + "," + v2 + "," + x1 + "," + y1 + "," + x2 + "," + y2;
        
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, mesajDate);
        producer.send(record);
    }

    /**
     * Desenează brațele (negru) și masele pendulului (albastru și roșu).
     */
    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(3));

        int x0 = 400, y0 = 200;
        int x1 = (int) (x0 + l1 * Math.sin(a1));
        int y1 = (int) (y0 + l1 * Math.cos(a1));
        int x2 = (int) (x1 + l2 * Math.sin(a2));
        int y2 = (int) (y1 + l2 * Math.cos(a2));

        // Desenare brate
        g.setColor(Color.BLACK);
        g.drawLine(x0, y0, x1, y1);
        g.drawLine(x1, y1, x2, y2);

        // Desenare mase
        g.setColor(Color.BLUE);
        g.fillOval(x1 - 12, y1 - 12, 24, 24);
        g.setColor(Color.RED);
        g.fillOval(x2 - 12, y2 - 12, 24, 24);
    }
}