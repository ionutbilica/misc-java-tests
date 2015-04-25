package ro.bilica.test.car.rental;

import java.util.HashMap;
import java.util.Map;

public class CarRental {
	
	private final long maxRentalLength;
	private final Map<String, Long> carToRentalTime;
	
	public CarRental(long maxRentalLength) {
		this.maxRentalLength = maxRentalLength;
		carToRentalTime = new HashMap<String, Long>();
		
		Thread thread = new ThreadExtension();

		thread.start();
	}
	
	public void rentCar(String registrationNumber) {
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
		System.out.println("Am inchiriat masina cu numarul: " + registrationNumber + " la " + System.currentTimeMillis());
	}
	
	public void returnCar(String registrationNumber) {
		 synchronized(carToRentalTime) {
			 carToRentalTime.remove(registrationNumber);
		 }
		System.out.println("Masina cu numarul: " + registrationNumber + " a fost returnata la " + System.currentTimeMillis());
	}
	
	private final class ThreadExtension extends Thread {
		public void run(){
			while(true){
				try {
					sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("checking..");
				for (Map.Entry<String, Long> entry : carToRentalTime.entrySet()) {
				    String key = entry.getKey();
				    Long value = entry.getValue();
				    if(System.currentTimeMillis() - value > maxRentalLength){
				    	System.out.println("			Masina cu numarul: " + key + " nu a fost returnata la timp!");
				    }
				}
			}
		}
	}
}
