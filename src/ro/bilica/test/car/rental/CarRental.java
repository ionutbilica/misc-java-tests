package ro.bilica.test.car.rental;

import java.util.HashMap;
import java.util.Map;

public class CarRental {
	
	private final long maxRentalLength;
	private final Map<String, Long> carToRentalTime;
	Thread thread;
	
	public CarRental(long maxRentalLength) {
		this.maxRentalLength = maxRentalLength;
		carToRentalTime = new HashMap<String, Long>();
	}
	
	public void rentCar(String registrationNumber) {
		if(carToRentalTime.isEmpty()){
			thread = new ThreadExtension();
			thread.start();	
		}
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
		System.out.println("I rented a car with registration number: " + registrationNumber + " at " + System.currentTimeMillis());
	}
	
	public void returnCar(String registrationNumber) {
		 Boolean removeSucceed = removeCarFromMap(registrationNumber);
		 if(removeSucceed){
			 System.out.println("The car with registration number " + registrationNumber + " was returned at " + System.currentTimeMillis());
		 }
	}

	private boolean removeCarFromMap(String registrationNumber) {
		Long removeSucceed;
		synchronized(carToRentalTime) {
			 removeSucceed = carToRentalTime.remove(registrationNumber);
		 }
		if(carToRentalTime.isEmpty()){
			synchronized(thread) {
				thread.interrupt();
			}
		}
		return removeSucceed != null;
	}
	
	private final class ThreadExtension extends Thread {
		public void run(){
			while (!isInterrupted()){
				try {
					synchronized(this) {
						sleep(300);	
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				verifyCars();
			}
		}

		private void verifyCars() {
			System.out.println("Check if rented cars were returned on time.");
			for (Map.Entry<String, Long> entry : carToRentalTime.entrySet()) {
			    String key = entry.getKey();
			    Long value = entry.getValue();
			    if(System.currentTimeMillis() - value > maxRentalLength){
			    	removeCarFromMap(key);
			    	/*synchronized(carToRentalTime) {
			    		carToRentalTime.remove(key);
			    	}*/
			    	System.out.println("The car with registration number " + key +" was NOT returned on time!");
			    }
			}
		}
	}
}
