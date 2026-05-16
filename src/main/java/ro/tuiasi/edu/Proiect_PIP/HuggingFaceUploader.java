package ro.tuiasi.edu.Proiect_PIP;

import java.io.File;
import java.io.IOException;

/**
 * Clasa HuggingFaceUploader se ocupa cu incarcarea automata a setului de date 
 * rezultat in urma simularii pe platforma HuggingFace.
 */

public class HuggingFaceUploader {

    public static void upload(String filePath, String repoId, String token) throws Exception {

        String repoUrl = "https://user:" + token + "@huggingface.co/datasets/" + repoId;
        String localDir = "hf_repo";

        // 1. Clone repo (dacă nu există deja)
        if (!new File(localDir).exists()) {
            runCommand("git clone " + repoUrl + " " + localDir);
        }

        // 2. Copiază fișierul în repo
        File source = new File(filePath);
        File dest = new File(localDir + "/" + source.getName());

        java.nio.file.Files.copy(
                source.toPath(),
                dest.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

     // 3. Commit & push
        
        runCommand("git -C " + localDir + " add .");
        runCommand("git -C " + localDir + " commit -m \"upload dataset\" ");
        runCommand("git -C " + localDir + " push origin main");

        System.out.println("Upload complet!");
    }

    private static void runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();

        // Pentru Windows (cmd)
        builder.command("cmd.exe", "/c", command);

        builder.inheritIO(); // ca să vezi outputul în consolă
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Eroare la comanda: " + command);
        }
    }
}