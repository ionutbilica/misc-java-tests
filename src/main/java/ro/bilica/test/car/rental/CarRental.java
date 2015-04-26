package ro.bilica.test.car.rental;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
			thread = new CheckerThread();
			thread.start();	
		}
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
		System.out.println("I rented a car with registration number: " + registrationNumber + " at " + System.currentTimeMillis());
	}
	
	public void returnCar(String registrationNumber) {
		synchronized (carToRentalTime) {
			if (carToRentalTime.containsKey(registrationNumber)) {
				System.out.println("Car returned on time: " + registrationNumber + ".");
				carToRentalTime.remove(registrationNumber);
			}
		}
	}
	
	private final class CheckerThread extends Thread {
		
		private boolean shouldStop;
		
		public CheckerThread() {
			shouldStop = false;
		}
		
		public void run(){
			while (!isInterrupted() && !shouldStop){
				sleepSomeTime();
				verifyCars();
				stopIfNoMoreCars();
			}
		}

		private void stopIfNoMoreCars() {
			synchronized (carToRentalTime) {
				if (carToRentalTime.isEmpty()) {
					shouldStop = true;
				}
			}
		}

		private void sleepSomeTime() {
			try {
				sleep(300);	
			} catch (InterruptedException e) {
				e.printStackTrace();
				shouldStop = true;
			}
		}

		private void verifyCars() {
			System.out.println("Check if rented cars were returned on time.");
			synchronized (carToRentalTime) {
				Set<String> unreturnedCars = new HashSet<String>();
				for (Map.Entry<String, Long> entry : carToRentalTime.entrySet()) {
					String registrationNumber = entry.getKey();
					Long rentalTime = entry.getValue();
					if(System.currentTimeMillis() - rentalTime > maxRentalLength){
						System.out.println("The car with registration number " + registrationNumber +" was NOT returned on time! Removing it.");
						unreturnedCars.add(registrationNumber);
					}
				}
				carToRentalTime.keySet().removeAll(unreturnedCars);
			}
		}
	}
}
