package ro.bilica.test.car.rental;

public class CarRentalClient {

	public static void main(String[] args) throws InterruptedException {
		CarRental carRental = new CarRental(5000);
		
		carRental.rentCar("DJ 10 YYG");
		carRental.rentCar("DJ 99 SUG");
		
		Thread.sleep(3000);
		
		carRental.returnCar("DJ 10 YYG");
		
		Thread.sleep(4000);
		
		carRental.returnCar("DJ 99 SUG");
	}
}
