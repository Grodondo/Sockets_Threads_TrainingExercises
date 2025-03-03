package paq1;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Procesador extends Thread{

	private int id;
	
	private boolean estado;    // true: robot en marcha;   false: robot apagado
	
	public int[] cadenaRob;
	public int[] cadenaPro;
	
	private Semaphore mutex;
	
	public Procesador(int id, int[] cadenaRob, int[] cadenaPro, Semaphore mutex) {
		super();
		this.id = id;
		this.cadenaRob = cadenaRob;
		this.cadenaPro = cadenaPro;
		this.mutex = mutex;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		Random r = new Random();
		
		this.estado=true;
		
		while(this.estado){
			
			int producto= r.nextInt(4)+1;
			
			int espera = r.nextInt(2)+1;
			
			esperar(1000*espera);
			
			if (!estado) break;
			
			try {
				mutex.acquire();
				
				System.out.println("El productor " + this.id + " produce el producto " + producto);
				
				int i=0;
				while(cadenaPro[i]!=0) { 
					i++;
					if (i==5) break;
				}
				
				if(i<5) {
				
					cadenaRob[i] = this.id;
					cadenaPro[i] = producto;
					System.out.println("El robot productor " + this.id + " ha colocado " + "el producto " + producto
							+ " en la posición " + i + " de la cadena de montaje");
					System.out.println("Cadena de montaje [Robots] " + Arrays.toString(cadenaRob));
					System.out.println("Cadena de montaje [Produc] " + Arrays.toString(cadenaPro) + "\n");

				}
				else {
					System.out.println("El procesador " + this.id + " no encuentra sitio para el producto\n");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally {
				mutex.release();
			}

				
		}
		
		System.out.println("El robot procesador " + this.id + " ha parado");
		
	}
	
	public void pararRobot() {
		
		this.estado= false;
		
	}
	
	public void esperar(long t) {
		
		try {
			sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}
