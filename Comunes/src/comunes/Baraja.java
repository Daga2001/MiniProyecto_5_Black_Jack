/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package comunes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * The Class Baraja.
 */
public class Baraja {
   private ArrayList<Carta> mazo;				// Cartas de juego
   private Random aleatorio;					// Generador de numeros random
   
   /**
    * Instantiates a new baraja.
    * Constructor de la calse
    * Sonstruye y asigna un valor a las cartas de juego
    */
   public Baraja() {
	   aleatorio = new Random();
	   mazo = new ArrayList<Carta>();
	   String valor;
	   for(int i=1;i<=4;i++) {
		   for(int j=2;j<=14;j++) {
			   switch(j) {
			   case 11: valor="J";break;
			   case 12: valor="Q";break;
			   case 13: valor="K";break;
			   case 14: valor="As";break;
			   default: valor= String.valueOf(j);break;
			   } 
			   switch(i) {
			   case 1: mazo.add(new Carta(valor,"C"));break;
			   case 2: mazo.add(new Carta(valor,"D"));break;
			   case 3: mazo.add(new Carta(valor,"P"));break;
			   case 4: mazo.add(new Carta(valor,"T"));break;
			   }
		   }
	   }
   }
   
   /**
    * Gets the carta.
    * Retirna una carta aleatoria del maso
    * @return the carta
    */
   public Carta getCarta() {
	   int index = aleatorio.nextInt(mazoSize());
	   Carta carta = mazo.get(index);
	   mazo.remove(index); //elimina del mazo la carta usada
	   return carta;
   }
   
   /**
    * Mazo size.
    * Retorna el tamaño del mazo
    * @return the int
    */
   public int mazoSize() {
	   return mazo.size();
   }
}
