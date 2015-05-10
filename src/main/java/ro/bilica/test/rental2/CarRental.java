package ro.bilica.test.rental2;

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
		
		thread = new CheckerThread();
		thread.start();	
	}
	
	public void rentCar(String registrationNumber) {
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
		System.out.println("I rented a car with registration number: " + registrationNumber + " at " + System.currentTimeMillis());
		synchronized (carToRentalTime) {
			carToRentalTime.notify();
		}
	}
	
	public void returnCar(String registrationNumber) {
		synchronized (carToRentalTime) {
			if (carToRentalTime.containsKey(registrationNumber)) {
				System.out.println("Car returned on time: " + registrationNumber + ".");
				carToRentalTime.remove(registrationNumber);
			}
		}
	}

	public void stop(){
		System.out.println("Stop!");
		synchronized (carToRentalTime) {
			carToRentalTime.notify();
		}
		synchronized (thread) {
			thread.interrupt();
		}
	}
	
	private final class CheckerThread extends Thread {
		public void run(){
			try {
				while (!isInterrupted()){
					sleepSomeTime();
					waitUntilThereAreRentedCars();
					verifyCars();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		
		private void waitUntilThereAreRentedCars() throws InterruptedException {
			synchronized (carToRentalTime) {
				if (carToRentalTime.isEmpty()) {
					carToRentalTime.wait();
				}
			}
		}

		private void sleepSomeTime() throws InterruptedException {
			sleep(300);	
		}

		private void verifyCars() {
			System.out.println("Check if rented cars were returned on time....");
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
