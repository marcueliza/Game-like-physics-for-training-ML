package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.*;
import java.awt.*;
import io.github.cdimascio.dotenv.Dotenv;

public class App {

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");

        // Pornim interfața grafică
        SwingUtilities.invokeLater(() -> {
            try {
                Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();

                // 1. Inițializăm Simulare și Monitorizare
                Simulare sim = new Simulare();
                // O pornim într-o fereastră separată imediat
                MonitorizareLive monitor = new MonitorizareLive();
                monitor.setLocation(10, 10);
                monitor.setVisible(true);

                // 2. Configurăm fereastra principală
                JFrame frame = new JFrame("Double Pendulum - Data Collector");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

                // Adăugăm pendulul în centru
                frame.add(sim, BorderLayout.CENTER);

                // 3. Bara de control cu butonul portocaliu
                JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                controlPanel.setBackground(new Color(45, 45, 45));
                
                JButton btnStop = new JButton("Stop Colectare & Upload Hugging Face");
                btnStop.setBackground(new Color(255, 87, 34));
                btnStop.setForeground(Color.WHITE);
                btnStop.setFont(new Font("Arial", Font.BOLD, 14));
                btnStop.setPreferredSize(new Dimension(350, 40));

                btnStop.addActionListener(e -> {
                    btnStop.setEnabled(false);
                    btnStop.setText("Se procesează upload-ul...");
                    
                    // Rulăm upload-ul pe un fir separat ca să nu înghețăm graficele
                    new Thread(() -> {
                        sim.stopRecording();
                        String token = dotenv.get("HF_TOKEN");
                        if (token == null) token = System.getenv("HF_TOKEN");

                        try {
                            HuggingFaceUploader.upload("dataset.csv", "SilviuMih21/ML_dataset", token);
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame, "Succes! Datele sunt pe Hugging Face.");
                                btnStop.setText("Upload Finalizat");
                            });
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame, "Eroare: " + ex.getMessage());
                                btnStop.setEnabled(true);
                            });
                        }
                    }).start();
                });

                controlPanel.add(btnStop);
                frame.add(controlPanel, BorderLayout.SOUTH);

                frame.setSize(900, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}