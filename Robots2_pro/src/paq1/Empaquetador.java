package paq1;


import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Empaquetador extends Thread{
	
		private int id;
		
		private int[] cadenaRob;
		private int[] cadenaPro;
		
		
		private Semaphore mutex;
		
		private boolean estado;

		
	public Empaquetador(int id, int[] cadenaRob, int[] cadenaPro, Semaphore mutex) {
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
		
		this.estado= true;
		
		while(estado) {
			
			int producto= r.nextInt(4)+1;
			
			if (!estado) break;
			
			try {
				
				mutex.acquire();
				System.out.println("El empaquetador " + this.id + " se dispone a empaquetar " +
				"un producto " + producto);
				
				int i=0;
				while(cadenaPro[i]!=producto) { 
					i++;
					if (i==5) break;
				}
				
				if(i<5) {
					
					System.out.println("Producto " + producto + " encontrado");
					char cadenaAst[]= {' ', ' ', ' ', ' ', ' '};
					cadenaRob[i]=0;
					cadenaPro[i]=0;
					cadenaAst[i]= '*';
					System.out.println("Cadena de montaje [ProEmp] " + Arrays.toString(cadenaAst));
					System.out.println("Cadena de montaje [Produc] " + Arrays.toString(cadenaPro) + "\n");
	
				}else {
					
					System.out.println("No se ha encontrado ningun producto " + producto + "\n");
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally {	
				mutex.release();
			}

				
			int espera= r.nextInt(2)+1;
			
			esperar(1000*espera);
			
			
		}
		
		System.out.println("El robot empaquetador " + this.id + " ha parado");
		
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
