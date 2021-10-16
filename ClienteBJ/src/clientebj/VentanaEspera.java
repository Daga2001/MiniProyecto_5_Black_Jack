/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaEspera.
 * Clase que se encarga de la ventana de espera en lo que el cliente estará hasta que el jueguo empiece.
 */
public class VentanaEspera extends JInternalFrame {
	private JLabel enEspera, jugador;
	
	/**
	 * Instantiates a new ventana espera.
	 * Constructor de la clase.
	 * @param jugador the jugador que estará en la ventana.
	 */
	public VentanaEspera(String jugador) {
        initInternalFrame(jugador);
		
		this.setTitle("Bienvenido a la sala de espera");
		this.pack();
		this.setResizable(true);
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inits the internal frame.
	 * Construye el JInternalFrame para el jugador.
	 * @param idJugador the id jugador
	 */
	private void initInternalFrame(String idJugador) {
		// TODO Auto-generated method stub
		this.getContentPane().setLayout(new FlowLayout());
		
		jugador = new JLabel(idJugador);
		Font font = new Font(Font.DIALOG,Font.BOLD,15);
		jugador.setFont(font);
		jugador.setForeground(Color.BLUE);
		add(jugador);
		enEspera = new JLabel();
		enEspera.setText("debes esperar al otro jugador...");
		enEspera.setFont(font);
		add(enEspera);
	}
	
	/**
	 * Cerrar sala espera.
	 * Cierra la ventana.
	 */
	public void cerrarSalaEspera() {
		this.dispose();
	}

}
