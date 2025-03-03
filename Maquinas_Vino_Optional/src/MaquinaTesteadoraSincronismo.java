
public class MaquinaTesteadoraSincronismo implements Runnable {

	
	public static boolean stop = false;
	private int numeroExtracciones;
	
	public MaquinaTesteadoraSincronismo() {
		numeroExtracciones = 0;
	}
	
	@Override
	public void run() {
		sincronismo();
		
	}
	
	private void sincronismo() {
		numeroExtracciones = 0;
		System.out.println("Thread MaquinaTesteadora running");
		
		while (!stop) {
			int randomChanceFail = (int) (Math.random() * 10); // Probabilidad de 10% de fallar
			
			if (randomChanceFail == 0) {
				try {
					//MaquinaSincronismo.mutex.acquire();

					System.out.println("Error en la muestra,maquinas limpiándose.");
					Thread.sleep(50);

				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				finally {
//					MaquinaSincronismo.mutex.release();
//				}
			}
			
			int randomTime = (int) (Math.random() * 11 + 30); // Tiempo aleatorio entre 30 y 40 ms
			
			try {
				Thread.sleep(randomTime); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

//			try {
//				MaquinaSincronismo.mutex.acquire(); 
				if (MaquinaSincronismo.litrosActual > 0) {
					MaquinaSincronismo.litrosActual -= 1; 
					numeroExtracciones++;
					//System.out.println("Extraido 1 Litro. Quedan: " + MaquinaSincronismo.litrosActual + " litros");
				} else {
					System.out.println("No hay vino por extraer.");
				}
//			} 
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			} finally {
//				MaquinaSincronismo.mutex.release(); 
//			}
		}
	}

	public int getNumeroExtracciones() {
		return numeroExtracciones;
	}

	public void setNumeroExtracciones(int numeroExtracciones) {
		this.numeroExtracciones = numeroExtracciones;
	}
	
	
	
}
