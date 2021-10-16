/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 * 			David Alberto Guzman Ardila - 1942789
 * 			Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */
package clientebj;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.Border;

/**
* The Class Title. displays titles with a text, size, background color, foreground and border defined by user.
*/
public class Title extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//attributes
	
	
	/**
	 * Instantiates a new Title.
	 * @param text the text
	 * @param size the size
	 * @param backgroundColor the background color
	 * @param foreGround the foreground color
	 * @param border the border 
	 * @param opaque type true here if you want to display a background color in this title.
	 */
	//methods
	public Title(String text, int size, Color backgroundColor, Color foreGround, Border border, boolean opaque) {
		this.setText(text);
		Font font = new Font(Font.SERIF, Font.BOLD+Font.ITALIC, size);
		this.setFont(font);
		this.setBorder(border);
		this.setBackground(backgroundColor);
		this.setForeground(foreGround);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setOpaque(opaque);
	}
}