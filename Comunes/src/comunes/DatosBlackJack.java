/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package comunes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The Class DatosBlackJack.
 * Clase encargada de los datos de los jugadores (Id, mano, apuesta, carta...)
 */
public class DatosBlackJack implements Serializable{
	private String[] idJugadores;														// Nombres de los jugadores
	private ArrayList<Carta> manoJugador1, manoJugador2, manoJugador3, manoDealer;		// Manos de los jugadores
	private int[] valorManos;															// Valor de las manos de los jugadores
	private double[] valorApuestas;														// Apuestas de los jugadores
	private Carta carta;																// Carta
	private String mensaje, jugador, jugadorEstado;										// Estado del jugador
	private volatile boolean reiniciar;													// reinicia o no el juego (false or true)
	
	/**
	 * Gets the jugador.
	 * Retorna el nombre del jugador
	 * @return the jugador
	 */
	public String getJugador() {
		return jugador;
	}
	
	/**
	 * Sets the jugador.
	 * Cambia el nombre del jugador
	 * @param jugador the new jugador
	 */
	public void setJugador(String jugador) {
		this.jugador = jugador;
	}
	
	/**
	 * Gets the jugador estado.
	 * Retorna el estado del jugador
	 * @return the jugador estado
	 */
	public String getJugadorEstado() {
		return jugadorEstado;
	}
	
	/**
	 * Sets the jugador estado.
	 * Cambia el estado del jugador
	 * @param jugadorEstado the new jugador estado
	 */
	public void setJugadorEstado(String jugadorEstado) {
		this.jugadorEstado = jugadorEstado;
	}
		
	/**
	 * Gets the id jugadores.
	 * Retorna todos los nombres de los jugadores en un array
	 * @return the id jugadores
	 */
	public String[] getIdJugadores() {
		return idJugadores;
	}
	
	/**
	 * Sets the id jugadores.
	 * Cambia el array contenedor de los nombres de los jugadores
	 * @param idJugadores the new id jugadores
	 */
	public void setIdJugadores(String[] idJugadores) {
		this.idJugadores = idJugadores;
	}
	
	/**
	 * Gets the valor apuestas.
	 * Retorna el valor de la apuesta del jugador
	 * @return the valor apuestas
	 */
	public double[] getValorApuestas() {
		return valorApuestas;
	}
	
	/**
	 * Sets the valor apuestas.
	 * Cambia el valor de la apuesta del jugador
	 * @param valorApuestas the new valor apuestas
	 */
	public void setValorApuestas(double[] valorApuestas) {
		this.valorApuestas = valorApuestas;
	}
	
	/**
	 * Gets the mano jugador 1.
	 * Retorna la mano de jugador 1
	 * @return the mano jugador 1
	 */
	public ArrayList<Carta> getManoJugador1() {
		return manoJugador1;
	}
	
	/**
	 * Sets the mano jugador 1.
	 * Cambia la mano de jugador 1
	 * @param manoJugador1 the new mano jugador 1
	 */
	public void setManoJugador1(ArrayList<Carta> manoJugador1) {
		this.manoJugador1 = manoJugador1;
	}
	
	/**
	 * Gets the mano jugador 2.
	 * Retorna la mano de jugador 2
	 * @return the mano jugador 2
	 */
	public ArrayList<Carta> getManoJugador2() {
		return manoJugador2;
	}
	
	/**
	 * Sets the mano jugador 2.
	 * Cambia la mano de jugador 2
	 * @param manoJugador2 the new mano jugador 2
	 */
	public void setManoJugador2(ArrayList<Carta> manoJugador2) {
		this.manoJugador2 = manoJugador2;
	}
	
	/**
	 * Gets the mano jugador 3.
	 * Retorna la mano de jugador 3
	 * @return the mano jugador 3
	 */
	public ArrayList<Carta> getManoJugador3() {
		return manoJugador3;
	}
	
	/**
	 * Sets the mano jugador 3.
	 * Cambia la mano de jugador 3
	 * @param manoJugador3 the new mano jugador 3
	 */
	public void setManoJugador3(ArrayList<Carta> manoJugador3) {
		this.manoJugador3 = manoJugador3;
	}
	
	/**
	 * Gets the mano dealer.
	 * Retorna la mano del dealer
	 * @return the mano dealer
	 */
	public ArrayList<Carta> getManoDealer() {
		return manoDealer;
	}
	
	/**
	 * Sets the mano dealer.
	 * Cambia la mano del dealer
	 * @param manoDealer the new mano dealer
	 */
	public void setManoDealer(ArrayList<Carta> manoDealer) {
		this.manoDealer = manoDealer;
	}
	
	/**
	 * Gets the mensaje.
	 * Retorna el mensaje del jugador
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}
	
	/**
	 * Sets the mensaje.
	 * Cambia el mensaje del jugador
	 * @param mensaje the new mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * Sets the valor manos.
	 * Cambia el valor de las manos de todos los jugadores
	 * @param valorManos the new valor manos
	 */
	public void setValorManos(int[] valorManos) {
		this.valorManos=valorManos;
	}
	
	/**
	 * Gets the valor manos.
	 * Retorna el valor de todas las manos de los jugadores
	 * @return the valor manos
	 */
	public int[] getValorManos() {
		return valorManos;	
	}
	
	/**
	 * Sets the carta.
	 * Cambia la carta del jugador
	 * @param carta the new carta
	 */
	public void setCarta(Carta carta) {
		this.carta=carta;
	}
	
	/**
	 * Gets the carta.
	 * Retorna la carta del jugador
	 * @return the carta
	 */
	public Carta getCarta() {
		return carta;
	}
	
	/**
	 * Checks if is number.
	 * Determina si un texto es un numero
	 * @param text the text
	 * @return true, if is number
	 */
	public boolean isNumber(String text) {
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
	 * Sets the reiniciar.
	 * Cambia el valor de reiniciar el juego
	 * @param reiniciar the new reiniciar
	 */
	public void setReiniciar(boolean reiniciar) {
		this.reiniciar=reiniciar;
	}
	
	/**
	 * Gets the reiniciar.
	 * Retorna el valor de reiniciar el juego
	 * @return the reiniciar
	 */
	public boolean getReiniciar() {
		return reiniciar;
	}
	
}
