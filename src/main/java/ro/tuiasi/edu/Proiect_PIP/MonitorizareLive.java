package ro.tuiasi.edu.Proiect_PIP;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MonitorizareLive extends JFrame {
    private XYSeries serieUnghi1 = new XYSeries("Unghi Braț 1");
    private XYSeries serieUnghi2 = new XYSeries("Unghi Braț 2"); // Seria nouă pentru brațul 2
    private KafkaConsumer<String, String> consumer;

    public MonitorizareLive() {
        super("Monitorizare Live Dublu Pendul (via Kafka)");
        
        // Configurăm Layout-ul să aibă 2 rânduri (un grafic sub altul)
        setLayout(new GridLayout(2, 1));

        // --- Grafic 1 (Braț 1) ---
        XYSeriesCollection dataset1 = new XYSeriesCollection(serieUnghi1);
        JFreeChart chart1 = ChartFactory.createXYLineChart("Oscilație Braț 1", "Timp (s)", "Unghi (rad)", dataset1);
        ChartPanel panel1 = new ChartPanel(chart1);
        add(panel1);

        // --- Grafic 2 (Braț 2) ---
        XYSeriesCollection dataset2 = new XYSeriesCollection(serieUnghi2);
        JFreeChart chart2 = ChartFactory.createXYLineChart("Oscilație Braț 2", "Timp (s)", "Unghi (rad)", dataset2);
        // Schimbăm culoarea liniei pentru al doilea grafic ca să le deosebim (opțional)
        chart2.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLUE);
        ChartPanel panel2 = new ChartPanel(chart2);
        add(panel2);

        // Setup Kafka Consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "monitor-group-" + System.currentTimeMillis());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("pendul-date"));

        setSize(800, 800); // Mărim fereastra ca să încapă ambele grafice
        setLocationRelativeTo(null);
        setVisible(true);

        new Thread(this::ascutaKafka).start();
    }

    private void ascutaKafka() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    // Split la mesajul primit: timp, unghi1, unghi2
                    String[] valori = record.value().split(",");
                    
                    // Verificăm dacă avem cel puțin 3 valori (timp, u1, u2)
                    if (valori.length >= 3) {
                        try {
                            double timp = Double.parseDouble(valori[0]);
                            double u1 = Double.parseDouble(valori[1]);
                            double u2 = Double.parseDouble(valori[2]);

                            SwingUtilities.invokeLater(() -> {
                                // Actualizăm Brațul 1
                                serieUnghi1.addOrUpdate(timp, u1);
                                if (serieUnghi1.getItemCount() > 100) serieUnghi1.remove(0);

                                // Actualizăm Brațul 2
                                serieUnghi2.addOrUpdate(timp, u2);
                                if (serieUnghi2.getItemCount() > 100) serieUnghi2.remove(0);
                            });
                        } catch (NumberFormatException e) {
                            // Ignorăm liniile de header sau datele malformate
                        }
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }
}