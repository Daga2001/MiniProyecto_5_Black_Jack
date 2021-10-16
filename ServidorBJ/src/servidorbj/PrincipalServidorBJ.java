/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package servidorbj;

/**
 * The Class PrincipalServidorBJ.
 * Clase principal que ejecuta el servidor
 * Clase principal, contiene el metodo main que inicia todo
 */
public class PrincipalServidorBJ {

	/**
	 * The main method.
	 * Inicia el servidor
	 * @param args the arguments
	 */
	public static void main(String[] args) {
        ServidorBJ servidor = new ServidorBJ();
        servidor.iniciar();
	}

}
