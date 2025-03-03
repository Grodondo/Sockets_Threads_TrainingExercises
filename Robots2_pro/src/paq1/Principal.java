// 3 robots productores y 3 empaquetadores

package paq1;

import java.util.concurrent.Semaphore;

public class Principal {


	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		int[] cadenaRob = new int[5];
		int[] cadenaPro = new int[5];
		
		Semaphore mutex = new Semaphore(1, true);
		
		Procesador p1 = new Procesador(1, cadenaRob, cadenaPro, mutex);
		Procesador p2 = new Procesador(2, cadenaRob, cadenaPro, mutex);
		Procesador p3 = new Procesador(3, cadenaRob, cadenaPro, mutex);
		
		Empaquetador e1 = new Empaquetador(1, cadenaRob, cadenaPro, mutex);
		Empaquetador e2 = new Empaquetador(2, cadenaRob, cadenaPro, mutex);
		Empaquetador e3 = new Empaquetador(3, cadenaRob, cadenaPro, mutex);		
		
		System.out.println("LOS ROBOTS PROCESADORES SE PONEN EN MARCHA\n");
		p1.start();
		p2.start();
		p3.start();
		
		Thread.sleep(2500);
		
		System.out.println("LOS ROBOTS EMPAQUETADORES SE PONEN EN MARCHA\n");
		
		e1.start();
		e2.start();
		e3.start();		
		
			Thread.sleep(4000);

		
		System.out.println("¡¡¡¡SE APAGA LA FABRICA!!!!");
		
		p1.pararRobot();
		p2.pararRobot();
		p3.pararRobot();
		e1.pararRobot();
		e2.pararRobot();
		e3.pararRobot();
		
	}

}
