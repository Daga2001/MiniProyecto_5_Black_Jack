/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import comunes.Carta;

// TODO: Auto-generated Javadoc
/**
 * The Class PanelJugador.
 * La clase que se encarga de manejar la información y elementos del panel de cada jugador.
 */
public class PanelJugador extends JPanel {

	//constantes de clase
	private static final int ANCHO = 206;
	private static final int ALTO = 89;

	//variables para control del graficado
	private ArrayList<Recuerdo> dibujoRecordar;
	private int x;
	private ImageIcon image;
	    
	/**
	 * Instantiates a new panel jugador.
	 * Constructor de la clase.
	 * @param datosJugador the datos jugador
	 */
	public PanelJugador(String datosJugador) {
		
		dibujoRecordar = new ArrayList<Recuerdo>();
		this.setPreferredSize(new Dimension(ANCHO,ALTO));
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(datosJugador);
		bordes.setTitleColor(Color.WHITE);
		this.setBorder(bordes);
		this.setBackground(new Color(0,0,0,64));

	}
	
	/**
	 * Pintar cartas inicio.
	 * Pinta en el panel las cartas iniciales del jugador.
	 * @param manoJugador Es el array que contiene las cartas del jugador.
	 */
	public void pintarCartasInicio(ArrayList<Carta> manoJugador) {
		dibujoRecordar = new ArrayList<Recuerdo>();
		x=5;
	    for(int i=0;i<manoJugador.size();i++) {
	    	Recuerdo recuerdo = new Recuerdo(manoJugador.get(i),x);
	    	dibujoRecordar.add(recuerdo);
	    	x+=15;
	    }
	    repaint();
	}
	
	/**
	 * Pintar la carta.
	 * Pinta la carta adicional en el panel junto con las anteriores.
	 * @param carta the carta nueva a pintar.
	 */
	public void pintarLaCarta (Carta carta) {
		dibujoRecordar.add(new Recuerdo(carta,x));
		x+=27;
		repaint();
	}
	
	/**
	 * Pintar cartas reinicio.
	 * Pinta las nuevas cartas que se usaran tras el reiniciar el juego.
	 * @param manoJugador Es el array que contiene las cartas del jugador.
	 */
	public void pintarCartasReinicio(ArrayList<Carta> manoJugador) {
		dibujoRecordar = new ArrayList<Recuerdo>();
		x=5;
		for(int i=0;i<manoJugador.size();i++) {
	    	dibujoRecordar.add(new Recuerdo(manoJugador.get(i),x));
	    	x+=15;
	    }
	    repaint();  
	}
	
	/**
	 * Limpiar.
	 * Reestablece el array de cartas recordadas.
	 */
	public void limpiar() {
		dibujoRecordar = new ArrayList<Recuerdo>(); 
		this.revalidate();
	}

	/**
	 * Paint component.
	 * Adiciona las imagenes correspondientes a las cartas.
	 * @param g the g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font =new Font(Font.DIALOG,Font.BOLD,12);
		g.setFont(font);
				
		//pinta la mano inicial
		for(int i=0;i<dibujoRecordar.size();i++) {
			String card = dibujoRecordar.get(i).getCartaRecordar();
			int x = dibujoRecordar.get(i).getxRecordar();
			int y = 17;
			int sizeX = 55;
			int sizeY = 65;
			image = new ImageIcon(this.getClass().getClassLoader().getResource(String.format("%s.png", card)));
			image = new ImageIcon(image.getImage().getScaledInstance(sizeX, sizeY, Image.SCALE_DEFAULT));
			g.drawImage(image.getImage(), x , y, image.getImageObserver());
		}
		this.revalidate();	
	}
	
	/**
	 * The Class Recuerdo.
	 * Clase privada para el manejo y almacenamiento de las manos inmediatamente anteriores.
	 */
	private class Recuerdo{

		private Carta cartaRecordar;
		private int xRecordar;

		/**
		 * Instantiates a new recuerdo.
		 * Constructor de la clase.
		 * @param cartaRecordar the carta recordar
		 * @param xRecordar the x recordar
		 */
		public Recuerdo(Carta cartaRecordar, int xRecordar) {
			this.cartaRecordar = cartaRecordar;
			this.xRecordar = xRecordar;
		}

		/**
		 * Gets the carta recordar.
		 * Retorna la carta en formato de String.
		 * @return the carta recordar
		 */
		public String getCartaRecordar() {
			return cartaRecordar.toString();
		}

		/**
		 * Gets the x recordar.
		 * Retorna su posición X.
		 * @return the x recordar
		 */
		public int getxRecordar() {
			return xRecordar;
		}
	}

}