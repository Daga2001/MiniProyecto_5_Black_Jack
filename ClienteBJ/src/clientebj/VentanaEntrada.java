/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaEntrada.
 * Clase que se encarga de gestionar la ventana cuando el jugador ingresa al juego.
 */
public class VentanaEntrada extends JInternalFrame {
	
	private JLabel bienvenida, labelNombre, labelApuesta;
	private JPanel ingreso;
	private JTextField nombreJugador;
	private JTextField apuestaJugador;
	private JButton ingresar;
	private VentanaEspera ventanaEspera;
	private ClienteBlackJack cliente;
	private Escucha escucha;
	
	/**
	 * Instantiates a new ventana entrada.
	 * Constructor de la clase.
	 * @param cliente the cliente que entra.
	 */
	public VentanaEntrada(ClienteBlackJack cliente) {
		this.cliente=cliente;
		initInternalFrame();
		
		this.setTitle("Bienvenido a Black Jack");
		this.pack();
		this.setLocation((ClienteBlackJack.WIDTH-this.getWidth())/2, 
				         (ClienteBlackJack.HEIGHT-this.getHeight())/2);
		this.show();
	}

	/**
	 * Inits the internal frame.
	 * Inicia el JInternalFrame y le pide los datos para registrar el cliente.
	 */
	private void initInternalFrame() {
		// TODO Auto-generated method stub
		escucha = new Escucha();
		this.getContentPane().setLayout(new BorderLayout());
		bienvenida = new JLabel("Registre su nombre para ingresar");
		add(bienvenida, BorderLayout.NORTH);

		ingreso = new JPanel(); 
		labelNombre = new JLabel("Nombre:"); 
		nombreJugador =	new JTextField(10); 
		labelApuesta = new JLabel("Apuesta:");
		apuestaJugador = new JTextField(10);
		ingresar = new JButton("Ingresar");
		ingresar.addActionListener(escucha);
		ingreso.add(labelNombre); ingreso.add(nombreJugador); 
		ingreso.add(labelApuesta); ingreso.add(apuestaJugador);
		ingreso.add(ingresar);
		add(ingreso,BorderLayout.CENTER);
	}
	
	/**
	 * Gets the container frames.
	 * Obtiene en quién está contenido.
	 * @return the container frames
	 */
	private Container getContainerFrames() {
		return this.getParent();
	}
    
	/**
	 * Cerrar ventana entrada.
	 * Cierra la ventana.
	 */
	private void cerrarVentanaEntrada() {
		this.dispose();
	}
	
	/**
	 * Checks if is number.
	 * Revisa si el dato ingresado como string es número.
	 * @param text the text
	 * @return true, if is number
	 */
	private boolean isNumber(String text) {
		if(text == null || text.length() == 0) {
			return false;
		}
		try {
			@SuppressWarnings("unused")
			double number = Double.parseDouble(text);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * The Class Escucha.
	 * Clase privada que implementa el actionListener.
	 */
	private class Escucha implements ActionListener{
		
		/**
		 * Action performed.
		 * Identifica si el cliente ha ingresado mal los datos o procede a crear la ventana de espera y destruir esta.
		 * @param arg0 the arg 0
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//cargar Sala de Espera y cerrar Ventana Entrada
			if(nombreJugador.getText().length()==0) {
				JOptionPane.showMessageDialog(null, "Debes ingresar un nombre para identificarte!!");
			}
			if(!isNumber(apuestaJugador.getText()) || apuestaJugador.getText().length()==0){
				JOptionPane.showMessageDialog(null, "Debes ingresar un valor númerico en la apuesta!!");
			}
			else {
				cliente.setIdYo(nombreJugador.getText());
				cliente.setApuestaYo(apuestaJugador.getText());
				ventanaEspera = new VentanaEspera(nombreJugador.getText());
				getContainerFrames().add(ventanaEspera);
				cliente.buscarServidor();
                cerrarVentanaEntrada();
			}	
		}
	}
	

}
