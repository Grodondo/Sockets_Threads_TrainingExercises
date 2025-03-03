import java.util.Scanner;

public class main {

	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		final int nMaquinas = 3;
		MaquinaSincronismo maquinaS[] = new MaquinaSincronismo[nMaquinas];
		Maquina maquina[] = new Maquina[nMaquinas];
		MaquinaTesteadoraSincronismo maquinaTesteadora = new MaquinaTesteadoraSincronismo();
		Thread t[] = new Thread[nMaquinas];
		Thread trS = new Thread(maquinaTesteadora);
		
		System.out.println("MÁQUINAS CON SINCRONISMO");
		
		long startTime = System.currentTimeMillis(); 
		
		for (int i = 0; i < maquinaS.length; i++) {
			maquinaS[i] = new MaquinaSincronismo();
		
			t[i] = new Thread(maquinaS[i]);
			t[i].start();
		}
		trS.start();
		
		try {
			for (int i = 0; i < maquinaS.length; i++) {
				t[i].join();
			}
			MaquinaTesteadoraSincronismo.stop = true;
			trS.join();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}

		long elapsedTime = System.currentTimeMillis() - startTime;
		
		for (int i = 0; i < maquinaS.length; i++) {
			maquinaS[i].mostrarDatos();
		}
		
		System.out.println("Total contenedor: " + MaquinaSincronismo.litrosActual);
		int totalVertido = 0;
		for (int i = 0; i < maquinaS.length; i++) {
			totalVertido += maquinaS[i].getLitrosVertidos();
		}
		System.out.println("Total vertido: " + totalVertido);
		System.out.println("Muestras tomadas: " + maquinaTesteadora.getNumeroExtracciones());
		System.out.println("Tiempo: " + elapsedTime + " ms");
		
		//----------------------------------------------------------------------------
		System.out.println("Press enter to continue");
		sc.nextLine();
		System.out.println("MÁQUINAS SIN SINCRONISMO");
		
		MaquinaTesteadora maquinaTesteadora2 = new MaquinaTesteadora();
		Thread tr = new Thread(maquinaTesteadora2);
		
		startTime = System.currentTimeMillis(); 
		
		// STARTS THREADS
		for (int i = 0; i < maquina.length; i++) {
			maquina[i] = new Maquina();
		
			t[i] = new Thread(maquina[i]);
			t[i].start();
		}
		tr.start();
		
		// JOINS
		try {
			for (int i = 0; i < maquina.length; i++) {
				t[i].join();
			}
			MaquinaTesteadora.stop = true;
			tr.join();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		elapsedTime = System.currentTimeMillis() - startTime;
		
		for (int i = 0; i < maquina.length; i++) {
			maquinaS[i].mostrarDatos();
		}
		
		System.out.println("Total contenedor: " + MaquinaSincronismo.litrosActual);
		totalVertido = 0;
		for (int i = 0; i < maquina.length; i++) {
			totalVertido += maquina[i].getLitrosVertidos();
		}
		System.out.println("Total vertido: " + totalVertido);
		System.out.println("Muestras tomadas: " + maquinaTesteadora2.getNumeroExtracciones());
		System.out.println("Tiempo: " + elapsedTime + " ms");
	}

}
