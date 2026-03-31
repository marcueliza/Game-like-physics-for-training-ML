package ro.tuiasi.edu.Proiect_PIP;

import javax.swing.JFrame;


public class App 
{
	public static void main(String[] args) {
		
        JFrame frame = new JFrame("Simulator pendul");
        frame.add(new Simulare());
        frame.setSize(800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
