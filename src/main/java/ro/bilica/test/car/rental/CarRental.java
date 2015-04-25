package ro.bilica.test.car.rental;

import java.util.HashMap;
import java.util.Map;

public class CarRental {
	
	private final long maxRentalLength;
	private Map<String, Long> carToRentalTime;
	
	public CarRental(long maxRentalLength) {
		this.maxRentalLength = maxRentalLength;
		carToRentalTime = new HashMap<String, Long>();

	}
	
	public void rentCar(String registrationNumber) {
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
	}
	
	public void returnCar(String registrationNumber) {
		
	}
}
