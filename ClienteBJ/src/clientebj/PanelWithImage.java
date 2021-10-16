/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class PanelWithImage.
 * Clase que crea un panel con una imagen como fondo.
 */
public class PanelWithImage extends JPanel {
	
	private ImageIcon image;
	
	/**
	 * Instantiates a new panel with image.
	 * Constructor de la clase.
	 */
	public PanelWithImage() {
		image = new ImageIcon(this.getClass().getClassLoader().getResource("Mesa.jpg"));
	}
	
	/**
	 * Paint component.
	 * Dibuja la imagen.
	 * @param g the g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		g.drawImage(image.getImage(), 0, 0, 640, 360, image.getImageObserver());
    }
}
