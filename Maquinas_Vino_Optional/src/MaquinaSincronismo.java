import java.util.concurrent.Semaphore;

public class MaquinaSincronismo implements Runnable{

	public static int litrosActual = 0;
    public static final int litrosMaximos = 5001;
    
    private int litrosProducidos;
    private int litrosVertidos;
    
    private static int nMaquina;
    private int numeroMaquina;
    
    //public static final Semaphore mutex = new Semaphore(1, true);
    
	public MaquinaSincronismo() {
		this.litrosProducidos = 0;
		this.litrosVertidos = 0;
		this.numeroMaquina = nMaquina++;
	}
	
	
	public synchronized void producirVino() {
		
		boolean contenedorLleno = false;
		
		while (!contenedorLleno) {
		
			int randomValue = (int) (Math.random() * 3 + 1);
			
//			try {
//				mutex.acquire();
			synchronized (MaquinaSincronismo.class) {
				if(litrosActual+randomValue >= litrosMaximos) {
					contenedorLleno = true;
					int restante = litrosMaximos - (litrosActual+randomValue);
					restante = Math.abs(restante);
					litrosVertidos += randomValue - restante;
					litrosActual += randomValue - restante;
				}else {
					litrosVertidos += randomValue;
					litrosActual += randomValue;
				}
				
				litrosProducidos += randomValue;
			}
				//System.out.println(litrosActual);
//			} 
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			} finally {
//				mutex.release();
//			}

			try {
				Thread.sleep(randomValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void mostrarDatos() {
        System.out.println((litrosActual == (litrosMaximos-1)) ? "Contenedor lleno, cantidad Contenedor: " + litrosActual : "Contenedor por llenar, cantidad Contenedor: " + litrosActual);
		System.out.println("Litros producidos por la máquina " + this.numeroMaquina + ": " + this.litrosProducidos);
        System.out.println("Litros vertidos por la máquina " + this.numeroMaquina + ": " + this.litrosVertidos);
        System.out.println();
	}
	
	@Override
	public void run() {
		producirVino();
		MaquinaTesteadoraSincronismo.stop = true;
	}


	public int getLitrosVertidos() {
		return litrosVertidos;
	}
	
	
	
}
