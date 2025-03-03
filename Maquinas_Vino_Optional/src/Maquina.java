

public class Maquina implements Runnable{

	public static int litrosActual = 0;
    public static final int litrosMaximos = 5000;
    
    private int litrosProducidos;
    private int litrosVertidos;
    
    private static int nMaquina;
    private int numeroMaquina;
    
	public Maquina() {
		this.litrosProducidos = 0;
		this.litrosVertidos = 0;
		this.numeroMaquina = nMaquina++;
	}
	
	
	public void producirVino() {
		
		boolean contenedorLleno = false;
		
		while (!contenedorLleno) {
		
			int randomValue = (int) (Math.random() * 3 + this.numeroMaquina + 1);
			
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
			
			//System.out.println(litrosActual);
			


			try {
				Thread.sleep(randomValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void mostrarDatos() {
        System.out.println((litrosActual == litrosMaximos) ? "Contenedor lleno, cantidad Contenedor: " + litrosActual : "Contenedor por llenar, cantidad Contenedor: " + litrosActual);
		System.out.println("Litros producidos por la máquina " + this.numeroMaquina + ": " + this.litrosProducidos);
        System.out.println("Litros vertidos por la máquina " + this.numeroMaquina + ": " + this.litrosVertidos);
        System.out.println();
	}
	
	@Override
	public void run() {
		producirVino();
		MaquinaTesteadora.stop = true;
	}
	
	public int getLitrosVertidos() {
		return litrosVertidos;
	}
	
}

