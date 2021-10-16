/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import comunes.DatosBlackJack;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaSalaJuego. Es la clase que construye la ventana donde se
 * desarrollará el juego, incluye a los paneles de los jugadores y eventos que
 * suceden.
 */
public class VentanaSalaJuego extends JInternalFrame {

	private PanelJugador dealer, yo, jugador2, jugador3;
	private JTextArea areaMensajes;
	private JButton pedir, plantar;
	private JPanel mensajes, panelYo, panelBotones, yoFull, panelDealer, panelJugador2, panelJugador3;
	private JScrollPane scroll;
	private PanelWithImage background;
	private ImageIcon image;
	private String yoId, jugador2Id, jugador3Id;
	private volatile boolean cerrarConexion, modificarApuesta, pantallaApuestasDesplegada, finDeRonda;
	private double apuestaYo, apuestaOtroJugador, apuestaOtroJugador2;
	private Escucha escucha;

	/**
	 * Instantiates a new ventana sala juego. Constructor de la clase.
	 * 
	 * @param yoId                the yo id
	 * @param jugador2Id          the jugador 2 id
	 * @param jugador3Id          the jugador 3 id
	 * @param apuestaYo           the apuesta yo
	 * @param apuestaOtroJugador  the apuesta otro jugador
	 * @param apuestaOtroJugador2 the apuesta otro jugador 2
	 */
	public VentanaSalaJuego(String yoId, String jugador2Id, String jugador3Id, double apuestaYo,
			double apuestaOtroJugador, double apuestaOtroJugador2) {
		this.yoId = yoId;
		this.jugador2Id = jugador2Id;
		this.jugador3Id = jugador3Id;
		this.apuestaYo = apuestaYo;
		this.apuestaOtroJugador = apuestaOtroJugador;
		this.apuestaOtroJugador2 = apuestaOtroJugador2;
		this.cerrarConexion = false;
		this.modificarApuesta = false;
		this.pantallaApuestasDesplegada = false;
		this.finDeRonda = false;

		initGUI();

		// default window settings
		this.setTitle("Sala de juego BlackJack - Jugador: " + yoId);
		this.pack();
		this.setLocation(((ClienteBlackJack.WIDTH / 2) - (this.getWidth() / 2)) / 2,
				((ClienteBlackJack.HEIGHT / 2) - (this.getHeight() / 2)) / 2);
		this.setResizable(false);
		this.show();
		this.setOpaque(false);
	}

	/**
	 * Inits the GUI. Inicia y adiciona los componentes graficos a la ventana.
	 */
	private void initGUI() {
		// TODO Auto-generated method stub
		// set up JFrame Container y Layout

		// Create Listeners objects
		escucha = new Escucha();
		// Create Control objects

		background = new PanelWithImage();
		background.setLayout(new BorderLayout());

		// Set up JComponents
		panelDealer = new JPanel();
		dealer = new PanelJugador("Dealer");
		panelDealer.setOpaque(false);
		panelDealer.add(dealer);
		background.add(panelDealer, BorderLayout.NORTH);

		panelJugador2 = new JPanel();
		jugador2 = new PanelJugador(String.format("%s - apuesta: %s", jugador2Id, apuestaOtroJugador));
		panelJugador2.setOpaque(false);
		panelJugador2.add(jugador2);
		background.add(panelJugador2, BorderLayout.EAST);

		panelJugador3 = new JPanel();
		jugador3 = new PanelJugador(String.format("%s - apuesta: %s", jugador3Id, apuestaOtroJugador2));
		panelJugador3.setOpaque(false);
		panelJugador3.add(jugador3);
		background.add(panelJugador3, BorderLayout.SOUTH);

		mensajes = new JPanel();
		mensajes.setOpaque(false);
		areaMensajes = new AreaMensajes(8, 18);
		scroll = new JScrollPane(areaMensajes);
		scroll.setOpaque(false);
		Border blackline;
		blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(blackline, "Area de Mensajes");
		bordes.setTitleJustification(TitledBorder.CENTER);
		bordes.setTitleColor(Color.WHITE);
		scroll.setBorder(bordes);
		areaMensajes.setEditable(false);

		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
		mensajes.add(scroll);
		background.add(mensajes, BorderLayout.CENTER);

		panelYo = new JPanel();
		panelYo.setLayout(new BorderLayout());
		yo = new PanelJugador(String.format("%s - apuesta: %s", yoId, apuestaYo));
		panelYo.setOpaque(false);
		panelYo.add(yo);

		pedir = new JButton("Carta");
		pedir.setEnabled(false);
		pedir.addActionListener(escucha);
		plantar = new JButton("Plantar");
		plantar.setEnabled(false);
		plantar.addActionListener(escucha);
		panelBotones = new JPanel();
		panelBotones.setOpaque(false);
		panelBotones.add(pedir);
		panelBotones.add(plantar);

		yoFull = new JPanel();
		yoFull.setPreferredSize(new Dimension(206, 100));
		yoFull.setOpaque(false);
		yoFull.add(panelYo);
		yoFull.add(panelBotones);
		background.add(yoFull, BorderLayout.WEST);

		add(background);
	}

	/**
	 * Activar botones. Activa o desactivas los botones pedir y plantar deacuerdo al
	 * estado que reciban.
	 * 
	 * @param turno the turno
	 */
	public void activarBotones(boolean turno) {
		pedir.setEnabled(turno);
		plantar.setEnabled(turno);
	}

	/**
	 * Pintar cartas inicio. Pinta las cartas iniciales para cada jugador.
	 * 
	 * @param datosRecibidos the datos recibidos para pintar las cartas.
	 */
	public void pintarCartasInicio(DatosBlackJack datosRecibidos) {
		if (datosRecibidos.getIdJugadores()[0].equals(yoId)) {
			if (datosRecibidos.getIdJugadores()[1].equals(jugador2Id)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
			} else {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador3());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador2());
			}
		} else if (datosRecibidos.getIdJugadores()[0].equals(jugador2Id)) {
			if (datosRecibidos.getIdJugadores()[1].equals(yoId)) {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador2());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador3());
			} else {
				yo.pintarCartasInicio(datosRecibidos.getManoJugador3());
				jugador2.pintarCartasInicio(datosRecibidos.getManoJugador1());
				jugador3.pintarCartasInicio(datosRecibidos.getManoJugador2());
			}
		} else {
			if (datosRecibidos.getIdJugadores()[0].equals(jugador3Id)) {
				if (datosRecibidos.getIdJugadores()[1].equals(yoId)) {
					yo.pintarCartasInicio(datosRecibidos.getManoJugador2());
					jugador2.pintarCartasInicio(datosRecibidos.getManoJugador3());
					jugador3.pintarCartasInicio(datosRecibidos.getManoJugador1());
				} else {
					yo.pintarCartasInicio(datosRecibidos.getManoJugador3());
					jugador2.pintarCartasInicio(datosRecibidos.getManoJugador2());
					jugador3.pintarCartasInicio(datosRecibidos.getManoJugador1());
				}
			}
		}
		dealer.pintarCartasInicio(datosRecibidos.getManoDealer());

		areaMensajes.append(datosRecibidos.getMensaje() + "\n");
	}

	/**
	 * Pintar cartas reinicio. Pinta las cartas nuevas después del reinicio.
	 * 
	 * @param datosRecibidos the datos recibidos
	 */
	public void pintarCartasReinicio(DatosBlackJack datosRecibidos) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (datosRecibidos.getIdJugadores()[0].equals(yoId)) {
					if (datosRecibidos.getIdJugadores()[1].equals(jugador2Id)) {
						yo.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador2());
						jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador3());
					} else {
						yo.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador3());
						jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador2());
					}
				} else if (datosRecibidos.getIdJugadores()[0].equals(jugador2Id)) {
					if (datosRecibidos.getIdJugadores()[1].equals(yoId)) {
						yo.pintarCartasReinicio(datosRecibidos.getManoJugador2());
						jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador3());
					} else {
						yo.pintarCartasReinicio(datosRecibidos.getManoJugador3());
						jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador2());
					}
				} else {
					if (datosRecibidos.getIdJugadores()[0].equals(jugador3Id)) {
						if (datosRecibidos.getIdJugadores()[1].equals(yoId)) {
							yo.pintarCartasReinicio(datosRecibidos.getManoJugador2());
							jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador3());
							jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						} else {
							yo.pintarCartasReinicio(datosRecibidos.getManoJugador3());
							jugador2.pintarCartasReinicio(datosRecibidos.getManoJugador2());
							jugador3.pintarCartasReinicio(datosRecibidos.getManoJugador1());
						}
					}
				}
				dealer.pintarCartasReinicio(datosRecibidos.getManoDealer());

			}
		});

	}

	/**
	 * Limpiar. Limpia los paneles.
	 */
	public void limpiar() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				yo.limpiar();
				jugador2.limpiar();
				dealer.limpiar();
			}

		});
	}

	/**
	 * Pintar turno. Dependiendo de la información recibida activara botones, pasará
	 * de turno o reiniciará la ronda del jugador.
	 * 
	 * @param datosRecibidos the datos recibidos
	 */
	public void pintarTurno(DatosBlackJack datosRecibidos) {
		areaMensajes.append(datosRecibidos.getMensaje() + "\n");
		ClienteBlackJack cliente = (ClienteBlackJack) this.getTopLevelAncestor();
		actualizarPanelesJugadores();

		if (datosRecibidos.getReiniciar() == true) {
			restart();
			activarBotones(false);
			areaMensajes.setText(datosRecibidos.getMensaje() + "\n");
			limpiar();
			pintarCartasReinicio(datosRecibidos);
		}

		if (datosRecibidos.getJugador().contentEquals(yoId)) {
			if (datosRecibidos.getJugadorEstado().equals("iniciar")) {
				activarBotones(true);
			} else {
				if (datosRecibidos.getJugadorEstado().equals("plantó")) {
					cliente.setTurno(false);
				} else {
					yo.pintarLaCarta(datosRecibidos.getCarta());
					if (datosRecibidos.getJugadorEstado().equals("voló")) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								activarBotones(false);
								cliente.setTurno(false);
							}
						});
					}
				}
			}
		} else if (datosRecibidos.getJugador().contentEquals(jugador2Id)) {
			// mensaje para PanelJuego jugador2
			if (datosRecibidos.getJugadorEstado().equals("sigue") || datosRecibidos.getJugadorEstado().equals("voló")) {
				jugador2.pintarLaCarta(datosRecibidos.getCarta());
			}
		} else {// movidas de los otros jugadores
			if (datosRecibidos.getJugador().equals(jugador3Id)) {
				// mensaje para PanelJuego jugador2
				if (datosRecibidos.getJugadorEstado().equals("sigue")
						|| datosRecibidos.getJugadorEstado().equals("voló")) {
					jugador3.pintarLaCarta(datosRecibidos.getCarta());
				}
			} else {
				// mensaje para PanelJuego dealer
				if (datosRecibidos.getJugadorEstado().equals("sigue")
						|| datosRecibidos.getJugadorEstado().equals("voló")
						|| datosRecibidos.getJugadorEstado().equals("plantó")) {
					dealer.pintarLaCarta(datosRecibidos.getCarta());
					checkIfRoundIsOver(datosRecibidos.getJugadorEstado(), datosRecibidos, cliente);
				}
			}
		}
	}

	/**
	 * Restart. Establece como false las variables modificarApuesta,
	 * pantallaApuestasDesplegada y finDeRonda.
	 */
	public void restart() {
		this.modificarApuesta = false;
		this.pantallaApuestasDesplegada = false;
		this.finDeRonda = false;
	}

	/**
	 * Enviar datos. Envia los datos pasados al servidor.
	 * 
	 * @param mensaje the mensaje
	 */
	private void enviarDatos(String mensaje) {
		// TODO Auto-generated method stub
		ClienteBlackJack cliente = (ClienteBlackJack) this.getTopLevelAncestor();
		cliente.enviarMensajeServidor(mensaje);
	}

	/**
	 * Actualizar paneles jugadores. Actualiza los paneles de los jugadores.
	 */
	public void actualizarPanelesJugadores() {
		// panel jugador 1
		TitledBorder bordes;
		bordes = BorderFactory.createTitledBorder(String.format("%s - apuesta: %s", yoId, apuestaYo));
		bordes.setTitleColor(Color.WHITE);
		yo.setBorder(bordes);
		// panel jugador 2
		bordes = BorderFactory.createTitledBorder(String.format("%s - apuesta: %s", jugador2Id, apuestaOtroJugador));
		bordes.setTitleColor(Color.WHITE);
		jugador2.setBorder(bordes);
		// panel jugador 3
		bordes = BorderFactory.createTitledBorder(String.format("%s - apuesta: %s", jugador3Id, apuestaOtroJugador2));
		bordes.setTitleColor(Color.WHITE);
		jugador3.setBorder(bordes);
		this.repaint();
		this.validate();
	}

	/**
	 * Determinar ganancias. Le envia datos al servidor y le pide calcula la ganacia
	 * 
	 * @param cliente the cliente el cliente a evaluar.
	 * @return the int el valor recibido de esta acción
	 */
	private int determinarGanancias(ClienteBlackJack cliente) {
		cliente.enviarMensajeServidor("calcular apuesta");
		cliente.enviarMensajeServidor(yoId);
		return cliente.calcularGanancias();
	}

	/**
	 * Show final bet. Muestra la información final del juego sobre el estado de la
	 * partida.
	 * 
	 * @param cliente the cliente
	 */
	private void showFinalBet(ClienteBlackJack cliente) {
		int valor = determinarGanancias(cliente);
		String title = "Valor final!";
		String message = "";
		if (valor > 0) {
			message = String.format("El dealer te pagará %s USD!", valor);
		} else {
			message = String.format("Has perdido la apuesta, ahora tienes %s USD!", valor);
		}
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show request message. Pregunta si se desea o no jugar otra partida, de ser si
	 * reinicia la ronda de lo contrario cierra la ventana.
	 * 
	 * @param datosRecibidos the datos recibidos
	 * @param cliente        the cliente
	 */
	public void showRequestMessage(DatosBlackJack datosRecibidos, ClienteBlackJack cliente) {
		String title = "Nueva ronda!";
		String message = "Quieres Iniciar Una nueva ronda?";
		int sizeX = 50, sizeY = 50;
		image = new ImageIcon(this.getClass().getClassLoader().getResource("requestNewRound.png"));
		image = new ImageIcon(image.getImage().getScaledInstance(sizeX, sizeY, Image.SCALE_DEFAULT));
		int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, image);
		switch (answer) {
		case JOptionPane.YES_OPTION:
			cliente.enviarMensajeServidor("reiniciar ronda");
			break;
		case JOptionPane.NO_OPTION:
			message = "Gracias por participar!!";
			title = "Hasta pronto, " + message;
			image = new ImageIcon(this.getClass().getClassLoader().getResource("ending.png"));
			JOptionPane.showMessageDialog(this, null, title, JOptionPane.INFORMATION_MESSAGE, image);
			cliente.enviarMensajeServidor("cerrar conexion");
			cliente.cerrarConexion();
			break;
		}
	}

	/**
	 * Check if round is over. Revisa si la ronda ha terminado y se la pasa a
	 * showRequestMessage
	 * 
	 * @param dealerStatus   the dealer status
	 * @param datosRecibidos the datos recibidos
	 * @param cliente        the cliente
	 */
	private void checkIfRoundIsOver(String dealerStatus, DatosBlackJack datosRecibidos, ClienteBlackJack cliente) {
		if ((dealerStatus.equals("voló") || dealerStatus.equals("plantó")) && !pantallaApuestasDesplegada) {
			this.finDeRonda = true;
			pantallaApuestasDesplegada = true;
			// Ventana de aviso del valor final de la apuesta
			showFinalBet(cliente);
			// Ventana de pregunta
			showRequestMessage(datosRecibidos, cliente);
		}
	}

	/**
	 * Se cerro conexion. Retorna la variable cerrarConexion
	 * 
	 * @return true, if successful
	 */
	public boolean seCerroConexion() {
		return this.cerrarConexion;
	}

	/**
	 * The Class Escucha. Clase privada que implementa ActionListener.
	 */
	private class Escucha implements ActionListener {

		/**
		 * Action performed. Da la acción (enviar al servidor) cuando escucha el
		 * accionar de pedir o plantar.
		 * 
		 * @param actionEvent the action event
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// TODO Auto-generated method stub
			if (actionEvent.getSource() == pedir) {
				System.out.println("Aaaaaa!");
				enviarDatos("pedir");
			} else {
				System.out.println("SIUUUU!");
				enviarDatos("plantar");
				activarBotones(false);
			}
		}
	}
	
	/**
	 * The Class AreaMensaje
	 * Crea un JTextArea y le añade un fondo semitransparente
	 */
	public class AreaMensajes extends JTextArea {

		private static final long serialVersionUID = 1L;
		
		/**
		 * Area de mensajes
		 * @param rows
		 * @param columns
		 */
		AreaMensajes(int rows, int columns) {
			this.setRows(rows);
			this.setColumns(columns);
			this.setOpaque(false);
			this.setForeground(new Color(255, 255, 255));
		}
		
		/**
		 * Grafica
		 * @param g
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(new Color(0, 0, 0, 64));
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}
}