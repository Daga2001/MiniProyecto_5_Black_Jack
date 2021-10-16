/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package comunes;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * The Class Carta.
 * Clase encargada de las cartas
 */
public class Carta implements Serializable{
    private String valor;			// Valor de la carta
    private String palo;			// Palo de la carta
	
    /**
     * Instantiates a new carta.
     * Constructor de la clase
     * Crea una carta y le asigna un valor y un palo
     * @param valor the valor
     * @param palo the palo
     */
    public Carta(String valor, String palo) {
		this.valor = valor;
		this.palo = palo;
	}

	/**
	 * Gets the valor.
	 * Retorna el valor de la carta
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Sets the valor.
	 * Cambia el valor de la carta
	 * @param valor the new valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Gets the palo.
	 * Retorna el valor de la carta
	 * @return the palo
	 */
	public String getPalo() {
		return palo;
	}

	/**
	 * Sets the palo.
	 * Cambia el valor de carta
	 * @param palo the new palo
	 */
	public void setPalo(String palo) {
		this.palo = palo;
	}
	
	/**
	 * To string.
	 * Retorna la carta con su valor y su palo, en forma de string
	 * @return the string
	 */
	public String toString() {
		return valor+palo;
	}
}
