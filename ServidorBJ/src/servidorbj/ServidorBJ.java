/*
 * Programación Interactiva.
 * Autores: Miguel Angel Fernandez Villaquiran - 1941923.
 *          David Alberto Guzman Ardila - 1942789
 *          Diego Fernando Chaverra - 1940322
 * Mini proyecto 5: Blackjack.
 */

package servidorbj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import comunes.Baraja;
import comunes.Carta;
import comunes.DatosBlackJack;

/**
 * The Class ServidorBJ.
 * Clase encargada de realizar la gestión del juego, esto es, el manejo de turnos y estado del juego.
 * También gestiona al jugador Dealer. 
 * El Dealer tiene una regla de funcionamiento definida:
 * Pide carta con 16 o menos y Planta con 17 o mas.
 */
public class ServidorBJ implements Runnable{
	// constantes para manejo de la conexion.
	public static final int PUERTO = 7377;											// Puerto de conexion
	public static final String IP = "127.0.0.1";									// IP local
	public static final int LONGITUD_COLA = 3;										// Clientes esperados

	// variables para funcionar como servidor
	private ServerSocket server;													// Servidor
	private Socket conexionJugador;													// Socket del servidor

	// variables para manejo de hilos
	private ExecutorService manejadorHilos;											// Ejecutor de threas
	private Lock bloqueoJuego;														// Sincronizador de threas
	private Condition esperarInicio, esperarTurno;									// Condiciones de sincronizacion
	private Jugador[] jugadores;													// Jugadores

	// variables de control del juego
	private String[] idJugadores;													// Nombres de los jugadores
	private volatile boolean finDeRonda, dealerTermina;								// Fin de la ronda (false or true)
	private int jugadorEnTurno, jugadoresQueReinician, jugadoresQueTerminan;		// Variables de control de orden en el juego
	private Baraja mazo;															// Cartas de juego
	private ArrayList<ArrayList<Carta>> manosJugadores;								// Cartas de los jugadores
	private ArrayList<Carta> manoJugador1;											// Cartas del jugador 1
	private ArrayList<Carta> manoJugador2;											// Cartas del jugador 1
	private ArrayList<Carta> manoJugador3;											// Cartas del jugador 1
	private ArrayList<Carta> manoDealer;											// Cartas del jugador 1
	private int[] valorManos;														// Sumas de las manos de cada jugador
	private double[] valorApuestas;													// Apuestas de cada jugador
	private DatosBlackJack datosEnviar;												// Datos de cada jugador
	
	/**
	 * Instantiates a new servidor BJ.
	 * Constructor de la clase
	 * Inicia los valores fundamentales de la clase, incluyendo el server
	 */
	public ServidorBJ() {
	    //inicializar variables de control del juego
		inicializarVariablesControlRonda();
	    //inicializar las variables de manejo de hilos
		inicializareVariablesManejoHilos();
		//crear el servidor
    	try {
    		mostrarMensaje("Iniciando el servidor...");
			server = new ServerSocket(PUERTO,LONGITUD_COLA);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Inicializare variables manejo hilos.
     * Crea las condiciones de blloqueo y el ejecutor de los jugadores
     */
    private void inicializareVariablesManejoHilos() {
		// TODO Auto-generated method stub
    	manejadorHilos = Executors.newFixedThreadPool(LONGITUD_COLA);
		bloqueoJuego = new ReentrantLock();
		esperarInicio = bloqueoJuego.newCondition();
		esperarTurno = bloqueoJuego.newCondition();
		bloqueoJuego.newCondition();
		jugadores = new Jugador[LONGITUD_COLA];	
	}

	/**
	 * Inicializar variables control ronda.
	 * Inicializa las variables principales del juego, los jugadores y sus manos
	 */
	private void inicializarVariablesControlRonda() {
    	 //Variables de control del juego.
		
		idJugadores = new String[LONGITUD_COLA];
		valorApuestas = new double[LONGITUD_COLA];
		valorManos = new int[LONGITUD_COLA+1];
		finDeRonda = false;
		dealerTermina = false;
		jugadoresQueReinician = 0;
		jugadoresQueTerminan = 0;
		
		mazo = new Baraja();
		Carta carta;
		
		manoJugador1 = new ArrayList<Carta>();
		manoJugador2 = new ArrayList<Carta>();
		manoJugador3 = new ArrayList<Carta>();
		manoDealer = new ArrayList<Carta>();
		
		//reparto inicial jugadores 1, 2 y 3
		for(int i=1;i<=2;i++) {
		  carta = mazo.getCarta();
		  manoJugador1.add(carta);
		  calcularValorMano(carta,0);
		  carta = mazo.getCarta();
		  manoJugador2.add(carta);
		  calcularValorMano(carta,1);
		  carta = mazo.getCarta();
		  manoJugador3.add(carta);
		  calcularValorMano(carta,2);
		}
		//Carta inicial Dealer
		carta = mazo.getCarta();
		manoDealer.add(carta);
		calcularValorMano(carta,3);
		
		//gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
		manosJugadores = new ArrayList<ArrayList<Carta>>(LONGITUD_COLA+1);
		manosJugadores.add(manoJugador1);
		manosJugadores.add(manoJugador2);
		manosJugadores.add(manoJugador3);
		manosJugadores.add(manoDealer);
	}

	/**
	 * Calcular valor mano.
	 * Calcula el valor de la mano de cada jugador, sumando los valores de sus cartas
	 * @param carta the carta
	 * @param i the i
	 */
	private void calcularValorMano(Carta carta, int i) {
    	
		if(carta.getValor().equals("As")) {
            if(valorManos[i]<=10) {
                valorManos[i]+=11;
            }
            else {
                valorManos[i]+=1;
            }

        }else {
				if(carta.getValor().equals("J") || carta.getValor().equals("Q")
						   || carta.getValor().equals("K")) {
					valorManos[i]+=10;
				}else {
					valorManos[i]+=Integer.parseInt(carta.getValor()); 
				}
		}
	}
	
	/**
	 * Iniciar.
	 * Inicia el servidor y ejecuta los jugadores despues de que se conecten
	 */
	public void iniciar() {
       	//esperar a los clientes
    	mostrarMensaje("Esperando a los jugadores...");
    	for(int i=0; i<LONGITUD_COLA;i++) {
    		try {
				conexionJugador = server.accept();
				jugadores[i] = new Jugador(conexionJugador,i);
	    		manejadorHilos.execute(jugadores[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}	
    	} 	
    }
    
	/**
	 * Mostrar mensaje.
	 * Muestra un mensaje en la consola
	 * @param mensaje the mensaje
	 */
	private void mostrarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	/**
	 * Iniciar ronda juego.
	 * Inicia la ronda de juego
	 */
	private void iniciarRondaJuego() {
		
		this.mostrarMensaje("bloqueando al servidor para despertar al jugador 1");
    	bloqueoJuego.lock();
    	
    	//despertar al jugador 1 porque es su turno
    	try {
    		this.mostrarMensaje("Despertando al jugador 1 para que inicie el juego");
        	jugadores[0].setSuspendido(false);
        	jugadores[1].setSuspendido(false);
        	esperarInicio.signalAll();
    	}catch(Exception e) {
    		
    	}finally {
    		this.mostrarMensaje("Desbloqueando al servidor luego de despertar al jugador 1 para que inicie el juego");
    		bloqueoJuego.unlock();
    	}			
	}
	
    /**
     * Se termino ronda.
     * Retorna si la ronda actual ha terminado
     * @return true, if successful
     */
    private boolean seTerminoRonda() {
       return this.finDeRonda;	
    }
    
    /**
     * Analizar mensaje.
     * Analiza los mensajes de entrada de los jugadores, y ejecuta una accion
     * Entradas: - (String) pedir : Pide carta
     * 			 - (String) plantar: No pide carta y se queda con su mano
     * @param entrada the entrada
     * @param indexJugador the index jugador
     */
    private void analizarMensaje(String entrada, int indexJugador) {
        //garantizar que solo se analice la petición del jugador en turno.
    	while(indexJugador!=jugadorEnTurno) {
    		bloqueoJuego.lock();
    		try {
				esperarTurno.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		bloqueoJuego.unlock();
    	}
    	
    	//valida turnos para jugador 0, 1 o 2
    	//Si pide carta
    	if(entrada.equals("pedir")) {
    		//dar carta 
    		mostrarMensaje("Se envió carta al jugador "+idJugadores[indexJugador]);
    		Carta carta = mazo.getCarta();
    		//adicionar la carta a la mano del jugador en turno
    		manosJugadores.get(indexJugador).add(carta);
    		calcularValorMano(carta, indexJugador);
    		
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
    		datosEnviar.setValorApuestas(valorApuestas);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		//determinar qué sucede con la carta dada en la mano del jugador y 
			//mandar mensaje a todos los jugadores
    		if(valorManos[indexJugador]>21) {
    			//jugador Voló
	    		datosEnviar.setMensaje(idJugadores[indexJugador]+" tienes "+valorManos[indexJugador]+" volaste :(");	
	    		datosEnviar.setJugadorEstado("voló");
	    		//Notifica a todos los jugadores
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		jugadores[2].enviarMensajeCliente(datosEnviar);
	    		jugadoresQueTerminan++;
	    		//Si es el jugador 1
	    		if(jugadorEnTurno==0) {
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
		    		datosEnviar.setValorApuestas(valorApuestas);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador(idJugadores[1]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[1]+" te toca jugar y tienes "+valorManos[1]);
					//Notifica a todos los jugadores
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);
					
					//levantar al jugador en espera de turno
					
					bloqueoJuego.lock();
		    		try {
						//esperarInicio.await();
						jugadores[0].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					}finally {
						bloqueoJuego.unlock();
					}
	        	}
	    		//Si es el jugador 2
	    		else if(jugadorEnTurno==1) {
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
		    		datosEnviar.setValorApuestas(valorApuestas);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador(idJugadores[2]);
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje(idJugadores[2]+" te toca jugar y tienes "+valorManos[2]);
					//Notifica a todos los jugadores
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);
					
					//levantar al jugador en espera de turno
					bloqueoJuego.lock();
		    		try {
						//esperarInicio.await();
						jugadores[1].setSuspendido(true);
						esperarTurno.signalAll();
						jugadorEnTurno++;
					}finally {
						bloqueoJuego.unlock();
					}
	        	} else {//era el jugador 3 entonces se debe iniciar el dealer
	        		//notificar a todos que le toca jugar al dealer
	        		datosEnviar = new DatosBlackJack();
		    		datosEnviar.setIdJugadores(idJugadores);
		    		datosEnviar.setValorApuestas(valorApuestas);
					datosEnviar.setValorManos(valorManos);
					datosEnviar.setJugador("dealer");
					datosEnviar.setJugadorEstado("iniciar");
					datosEnviar.setMensaje("Dealer se repartirá carta");
					//Notifica a todos los jugadores
					jugadores[0].enviarMensajeCliente(datosEnviar);
					jugadores[1].enviarMensajeCliente(datosEnviar);
					jugadores[2].enviarMensajeCliente(datosEnviar);
					
					iniciarDealer();
	        	}		
    		}else {//jugador no se pasa de 21 puede seguir jugando
    			datosEnviar.setCarta(carta);
    			datosEnviar.setJugador(idJugadores[indexJugador]);
    			datosEnviar.setMensaje(idJugadores[indexJugador]+" ahora tienes "+valorManos[indexJugador]);
	    		datosEnviar.setJugadorEstado("sigue");
	    		//Notifica a todos los jugadores
	    		jugadores[0].enviarMensajeCliente(datosEnviar);
	    		jugadores[1].enviarMensajeCliente(datosEnviar);
	    		jugadores[2].enviarMensajeCliente(datosEnviar);
	    		
    		}
    	}
    	//Si decide pantar
    	else if (entrada.equals("plantar")) {
    		//jugador en turno plantó
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setIdJugadores(idJugadores);
    		datosEnviar.setValorApuestas(valorApuestas);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setJugador(idJugadores[indexJugador]);
    		datosEnviar.setMensaje(idJugadores[indexJugador]+" se plantó");
    		datosEnviar.setJugadorEstado("plantó");
    		
    		jugadores[0].enviarMensajeCliente(datosEnviar);		    		
    		jugadores[1].enviarMensajeCliente(datosEnviar);
    		jugadores[2].enviarMensajeCliente(datosEnviar);
    		
    		jugadoresQueTerminan++;
    		
    		//notificar a todos el jugador que sigue en turno
    		//Si es el jugador 1
    		if(jugadorEnTurno==0) {
        		
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
	    		datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[1]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[1]+" te toca jugar y tienes "+valorManos[1]);
				//Notifica a todos los jugadores
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				
				//levantar al jugador en espera de turno
				
				bloqueoJuego.lock();
	    		try {
					//esperarInicio.await();
					jugadores[0].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				}finally {
					bloqueoJuego.unlock();
				}
        	}
    		//Si es el jugador 2
    		else if(jugadorEnTurno==1) {
        		
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
	    		datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador(idJugadores[2]);
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje(idJugadores[2]+" te toca jugar y tienes "+valorManos[2]);
				//Notifica a todos los jugadores
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				
				//levantar al jugador en espera de turno
				
				bloqueoJuego.lock();
	    		try {
					//esperarInicio.await();
					jugadores[1].setSuspendido(true);
					esperarTurno.signalAll();
					jugadorEnTurno++;
				}finally {
					bloqueoJuego.unlock();
				}
        	} else {//era el jugador 3 entonces se debe iniciar el dealer
        		//notificar a todos que le toca jugar al dealer
        		datosEnviar = new DatosBlackJack();
	    		datosEnviar.setIdJugadores(idJugadores);
	    		datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setJugador("dealer");
				datosEnviar.setJugadorEstado("iniciar");
				datosEnviar.setMensaje("Dealer se repartirá carta");
				//Notifica a todos los jugadores
				jugadores[0].enviarMensajeCliente(datosEnviar);
				jugadores[1].enviarMensajeCliente(datosEnviar);
				jugadores[2].enviarMensajeCliente(datosEnviar);
				
				iniciarDealer();
        	}	
    	}
   } 
    
    /**
     * Iniciar dealer.
     * Inicia el dealer, el cual es un thread
     */
    public void iniciarDealer() {
       //le toca turno al dealer.
    	Thread dealer = new Thread(this);
    	dealer.start();
    }
    
    /**
     * Close connection.
     * Cierra la conexion con los clientes
     */
    private void closeConnection() {
    	try {
    		server.close();
			conexionJugador.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * The Class Jugador.
     * Clase interna que maneja el servidor para gestionar la comunicación
     * con cada cliente Jugador que se conecte
     */
    private class Jugador implements Runnable{
       
	    private ObjectOutputStream out;							// Flujo de entrada
	    private ObjectInputStream in;							// Flujo de salida
	    private String entrada;									// Mensaje entrante
	    //variables de control
    	private int indexJugador;								// Identificador de jugador
	    private boolean suspendido;								// Esta en funcionamiento (false or true)
  
		/**
		 * Instantiates a new jugador.
		 * Constructor de la clase
		 * Inicia lo conexion con el cliente y los flujos e¿de entrada y salida
		 * @param conexionCliente the conexion cliente
		 * @param indexJugador the index jugador
		 */
		public Jugador(Socket conexionCliente, int indexJugador) {
			this.indexJugador = indexJugador;
			suspendido = true;
			//crear los flujos de E/S
			try {
				out = new ObjectOutputStream(conexionCliente.getOutputStream());
				out.flush();
				in = new ObjectInputStream(conexionCliente.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}	
				
		/**
		 * Sets the suspendido.
		 * Cambiar el estado del jugador
		 * @param suspendido the new suspendido
		 */
		private void setSuspendido(boolean suspendido) {
			this.suspendido = suspendido;
		}
		
		/**
		 * Calcular ganancias.
		 * Calcula si el jugador gana o no dinero
		 * Define cuanto gana en caso de que gane
		 * Determina el total a pagar a cada jugador dependiendo de su resultado en contra del Dealer
		 * @param idPlayer the id player
		 * @return the int
		 */
		private int calcularGanancias(String idPlayer) {
			int valorFinal = 0;
			int index = 0;
			for(int i = 0; i < idJugadores.length; i++) {
				if(idJugadores[i].equals(idPlayer)) {
					valorFinal = (int) valorApuestas[i];
					index = i;
					break;
				}
			}
			if(valorManos[LONGITUD_COLA] <= 21) {
                if(valorManos[LONGITUD_COLA] < valorManos[index] && valorManos[index] <= 21) {
                    if(valorManos[index] == 21 && manosJugadores.get(index).size() == 2) {
                        return (valorFinal*3/2);
                    }
                    else {
                        return (valorFinal*1/1);
                    }
                }
                if(valorManos[LONGITUD_COLA] == valorManos[index]) {
                    if(manosJugadores.get(index).size() == 2 && valorManos[index] == 21) {
                        return (valorFinal*3/2);
                    }
                    else {
                        return valorFinal;
                    }
                }
                else {
                    return 0;
                }
            }
            else {
                if(valorManos[index] > 21) {
                    return 0;
                }
                if(valorManos[index] == 21 && manosJugadores.get(index).size() == 2) {
                    return (valorFinal*3/2);
                }
                else {
                    return (valorFinal*1/1);
                }
            }
		}
		
		/**
		 * Reiniciar.
		 * Reinicia los valores del juego luego de acabada una partida
		 */
		public void reiniciar() {
			mostrarMensaje("Se reinició el juego");
			finDeRonda = false;
			dealerTermina = false;
			jugadoresQueReinician = 0;
			jugadoresQueTerminan = 0;
			
			Carta carta;
			manoJugador1 = new ArrayList<Carta>();
			manoJugador2 = new ArrayList<Carta>();
			manoJugador3 = new ArrayList<Carta>();
			manoDealer = new ArrayList<Carta>();
			manosJugadores = new ArrayList<ArrayList<Carta>>();
			mazo = new Baraja();
			
			for(int i = 0; i <= LONGITUD_COLA; i++) {
				valorManos[i] = 0;
			}
			
			//reparto inicial jugadores 1, 2 y 3
			for(int i=1;i<=2;i++) {
			  carta = mazo.getCarta();
			  manoJugador1.add(carta);
			  calcularValorMano(carta,0);
			  carta = mazo.getCarta();
			  manoJugador2.add(carta);
			  calcularValorMano(carta,1);
			  carta = mazo.getCarta();
			  manoJugador3.add(carta);
			  calcularValorMano(carta,2);
			}
			
			//Carta inicial Dealer
			carta = mazo.getCarta();
			manoDealer.add(carta);
			calcularValorMano(carta, LONGITUD_COLA);
			
			//gestiona las tres manos en un solo objeto para facilitar el manejo del hilo
			manosJugadores = new ArrayList<ArrayList<Carta>>(LONGITUD_COLA+1);
			manosJugadores.add(manoJugador1);
			manosJugadores.add(manoJugador2);
			manosJugadores.add(manoJugador3);
			manosJugadores.add(manoDealer);
			
		}
		
		/**
		 * Reiniciar ronda.
		 * Asigna los nuevos valores y turno de los jugadores al iniciar una nueva ronda
		 */
		private void reiniciarRonda() {
			mostrarMensaje("Server restarted the game!");
			//garantizar que solo se analice la petición del jugador en turno.
    		//---------------------------------------------------------------------------------
    		//Utiliza reiniciar y le envia al cliente (0) los nuevos valores, este mensaje lo lee al dar reiniciar en la vista
    		//Actualizar barajas jugadores.
    		reiniciar();
    		datosEnviar = new DatosBlackJack();
    		datosEnviar.setJugador(idJugadores[0]);
			datosEnviar.setManoDealer(manoDealer);
			datosEnviar.setManoJugador1(manoJugador1);
			datosEnviar.setManoJugador2(manoJugador2);
			datosEnviar.setManoJugador3(manoJugador3);
			datosEnviar.setIdJugadores(idJugadores);
			datosEnviar.setValorApuestas(valorApuestas);
			datosEnviar.setValorManos(valorManos);
			datosEnviar.setReiniciar(true);
			datosEnviar.setJugadorEstado("iniciar");
			datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
			
			for(int i = 0; i < LONGITUD_COLA; i++) {
				jugadores[i].enviarMensajeCliente(datosEnviar);
			}
			bloqueoJuego.lock();
    		try {
				//esperarInicio.await();
				jugadores[indexJugador].setSuspendido(true);
				esperarTurno.signalAll();
				jugadorEnTurno = 0;
			}finally {
				bloqueoJuego.unlock();
			}
    		for(int i=0; i<LONGITUD_COLA;i++) {
    			manejadorHilos.execute(jugadores[i]);
    		}
    		
		}
	   
		/**
		 * Run.
		 * Define el orden y los nombres de los jugadores, asi como sus datos en el juego
		 */
		public void run() {
			//procesar los mensajes eviados por el cliente
			
			//ver cual jugador es
			if(indexJugador==0) {
				//es jugador 1, debe ponerse en espera a la llegada del otro jugador
				
				try {
					//guarda el nombre del primer jugador
					idJugadores[0] = (String)in.readObject();
					valorApuestas[0] = Double.parseDouble((String) in.readObject());
					mostrarMensaje("Hilo establecido con jugador (1) "+idJugadores[0]);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 1");
				bloqueoJuego.lock(); //bloquea el servidor
				
				while(suspendido) {
					mostrarMensaje("Parando al Jugador 1 en espera del otro jugador...");
					try {
						esperarInicio.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 1");
						bloqueoJuego.unlock();
					}
				}
				
				//ya se conectó el otro jugador, 
				//le manda al jugador 1 todos los datos para montar la sala de Juego
				//le toca el turno a jugador 1
				
				mostrarMensaje("manda al jugador 1 todos los datos para montar SalaJuego");
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));
				datosEnviar.setManoJugador3(manosJugadores.get(2));
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
				enviarMensajeCliente(datosEnviar);
				jugadorEnTurno=0;
			}
			else if(indexJugador==1) {
				//es jugador 1, debe ponerse en espera a la llegada del otro jugador
				
				try {
					//guarda el nombre del primer jugador
					idJugadores[1] = (String)in.readObject();
					valorApuestas[1] = Double.parseDouble((String) in.readObject());
					mostrarMensaje("Hilo establecido con jugador (2) "+idJugadores[1]);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mostrarMensaje("bloquea servidor para poner en espera de inicio al jugador 2");
				bloqueoJuego.lock(); //bloquea el servidor
				
				while(suspendido) {
					mostrarMensaje("Parando al Jugador 2 en espera del otro jugador...");
					try {
						esperarInicio.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						mostrarMensaje("Desbloquea Servidor luego de bloquear al jugador 2");
						bloqueoJuego.unlock();
					}
				}
				
				//ya se conectó el otro jugador, 
				//le manda al jugador 1 todos los datos para montar la sala de Juego
				//le toca el turno a jugador 1
				
				mostrarMensaje("manda al jugador 1 todos los datos para montar SalaJuego");
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));			
				datosEnviar.setManoJugador3(manosJugadores.get(2));			
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
				enviarMensajeCliente(datosEnviar);
//				jugadorEnTurno=0;
				suspendido = true;
				mostrarMensaje("Bloquea al servidor para poner en espera de turno al jugador 2");
				bloqueoJuego.lock();
				try {
					mostrarMensaje("Pone en espera de turno al jugador 2");
					esperarTurno.await();
                    //
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					bloqueoJuego.unlock();
				}
			}
			else {
				   //Es jugador 3
				   //le manda al jugador 3 todos los datos para montar la sala de Juego
				   //jugador 3 debe esperar su turno
				try {
					idJugadores[2]=(String)in.readObject();
					valorApuestas[2] = Double.parseDouble((String) in.readObject());
					mostrarMensaje("Hilo jugador (3) "+idJugadores[2]);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mostrarMensaje("manda al jugador 3 el nombre del jugador 2");
				
				datosEnviar = new DatosBlackJack();
				datosEnviar.setManoDealer(manosJugadores.get(3));
				datosEnviar.setManoJugador1(manosJugadores.get(0));
				datosEnviar.setManoJugador2(manosJugadores.get(1));			
				datosEnviar.setManoJugador3(manosJugadores.get(2));			
				datosEnviar.setIdJugadores(idJugadores);
				datosEnviar.setValorApuestas(valorApuestas);
				datosEnviar.setValorManos(valorManos);
				datosEnviar.setMensaje("Inicias "+idJugadores[0]+" tienes "+valorManos[0]);
				enviarMensajeCliente(datosEnviar);
				
				iniciarRondaJuego(); //despertar al jugador 1 para iniciar el juego
				mostrarMensaje("Bloquea al servidor para poner en espera de turno al jugador 2");
				bloqueoJuego.lock();
				try {
					mostrarMensaje("Pone en espera de turno al jugador 3");
					esperarTurno.await();
					mostrarMensaje("Despierta de la espera de inicio del juego al jugador 1");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					bloqueoJuego.unlock();
				}	
			}
			
			while(!seTerminoRonda()) {
				try {
					entrada = (String) in.readObject();
					if(entrada.equals("cerrar conexion")) {
						closeConnection();
					}
					if(entrada.equals("reiniciar ronda")) {
						System.out.println("un jugador quiere reiniciar la ronda!");
						jugadoresQueReinician++;
//						leerSolicitud();
						if(jugadoresQueReinician == LONGITUD_COLA) {
							reiniciarRonda();
						}
					}
					if(entrada.equals("calcular apuesta")) {
						while(!dealerTermina || jugadoresQueTerminan < LONGITUD_COLA) {
							//do nothing
						}
						mostrarMensaje("Calculando Apuesta!");
						String idPlayer = (String) in.readObject();
						int finalValue = calcularGanancias(idPlayer);
						enviarMensajeCliente(finalValue);
					}
					if(!dealerTermina) {
						analizarMensaje(entrada,indexJugador);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					//controlar cuando se cierra un cliente
				}
			}
			//cerrar conexión
		}
		
		/**
		 * Enviar mensaje cliente.
		 * Envia mensajes al cliente por medio de los flijos E/S
		 * @param mensaje the mensaje
		 */
		public void enviarMensajeCliente(Object mensaje) {
			try {  
				mostrarMensaje("Se mandó mensaje al cliente");
				out.writeObject(mensaje);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
    }//fin inner class Jugador      

    /**
     * Run.
     * Jugador dealer emulado por el servidor
     * Ejecuta el delaer y sus funciones
     */
	public void run() {
		mostrarMensaje("Incia el dealer ...");
        while(!dealerTermina) {
		  	Carta carta = mazo.getCarta();
			//adicionar la carta a la mano del dealer
			manosJugadores.get(LONGITUD_COLA).add(carta);
			calcularValorMano(carta, 3);
			
			mostrarMensaje("El dealer recibe "+carta.toString()+" suma "+ valorManos[LONGITUD_COLA]);
			
    		datosEnviar = new DatosBlackJack();
			datosEnviar.setCarta(carta);
			datosEnviar.setJugador("dealer");
				
			if(valorManos[LONGITUD_COLA]<=16) {
				datosEnviar.setJugadorEstado("sigue");
				datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[LONGITUD_COLA]);
				mostrarMensaje("El dealer sigue jugando");
			}else {
				if(valorManos[LONGITUD_COLA]>21) {
					datosEnviar.setJugadorEstado("voló");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[LONGITUD_COLA]+" voló :(");
					mostrarMensaje("El dealer voló");
					dealerTermina = true;
				}else {
					datosEnviar.setJugadorEstado("plantó");
					datosEnviar.setMensaje("Dealer ahora tiene "+valorManos[LONGITUD_COLA]+" plantó");
					mostrarMensaje("El dealer plantó");
					dealerTermina = true;
				}
			}
			//envia la jugada a los otros jugadores
			datosEnviar.setCarta(carta);
			jugadores[0].enviarMensajeCliente(datosEnviar);
			jugadores[1].enviarMensajeCliente(datosEnviar);
			jugadores[2].enviarMensajeCliente(datosEnviar);
				
        }//fin while        
	}
    
}//Fin class ServidorBJ