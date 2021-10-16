/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.EventQueue;

import javax.swing.UIManager;


// TODO: Auto-generated Javadoc
/**
 * The Class PrincipalClienteBJ.
 * Clase principal del proyecto.
 */
public class PrincipalClienteBJ {
	
	/**
	 * The main method.
	 * Inicia el proyecto.
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		}catch(Exception e) {e.printStackTrace();}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				ClienteBlackJack cliente = new ClienteBlackJack();
			}		
		});
	}
}
