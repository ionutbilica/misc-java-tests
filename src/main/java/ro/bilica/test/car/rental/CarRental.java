package ro.bilica.test.car.rental;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class CarRental {

	Logger log;
	
	private final long maxRentalLength;
	private final Map<String, Long> carToRentalTime;
	
	public CarRental(long maxRentalLength) {
		this.maxRentalLength = maxRentalLength;
		carToRentalTime = new HashMap<String, Long>();
		
		Thread thread = new Thread(){
		    public void run(){
		    	while(true){
		    		for (Map.Entry<String, Long> entry : carToRentalTime.entrySet()) {
		    		    String key = entry.getKey();
		    		    Long value = entry.getValue();
		    		    if(System.currentTimeMillis() - value > maxRentalLength){
		    		    	System.out.println("			Masina cu numarul: " + key + " nu a fost returnata la timp!");
		    		    }
		    		}
		    	}
		    }
		  };

		  thread.start();
	}
	
	public void rentCar(String registrationNumber) {
		carToRentalTime.put(registrationNumber, System.currentTimeMillis());
		System.out.println("Am inchiriat masina cu numarul: " + registrationNumber + " la " + System.currentTimeMillis());
	}
	
	public void returnCar(String registrationNumber) {
		carToRentalTime.remove(registrationNumber);
		System.out.println("Masina cu numarul: " + registrationNumber + " a fost returnata la " + System.currentTimeMillis());
	}
}
