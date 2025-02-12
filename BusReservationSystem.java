import java.util.*;

// Class representing a customer
class Customer {
    String name, mobile, email, city;
    int age;

 // Constructor to initialize customer details
    public Customer(String name, String mobile, String email, String city, int age) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.age = age;
    }

// Method to return customer details as a string
    @Override
    public String toString() {
        return "Name: " + name + ", Mobile: " + mobile + ", Email: " + email + ", City: " + city + ", Age: " + age;
    }
}

// Class representing a bus
class Bus {
    String busNumber, startingPoint, endingPoint;
    int totalSeats;
    String startingTime;
    double fare;

// Constructor to initialize bus details
    public Bus(String busNumber, int totalSeats, String startingPoint, String endingPoint, String startingTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.startingTime = startingTime;
        this.fare = fare;
    }

   // Method to return bus details as a string
    @Override
    public String toString() {
        return "Bus Number: " + busNumber + ", Seats: " + totalSeats + ", From: " + startingPoint + " To: " + endingPoint + ", Time: " 
        + startingTime + ", Fare: " + fare;
    }
}

public class BusReservationSystem {

 // Data structures to store customers, buses, reservations, and waiting queue
    private static Stack<Customer> customerStack = new Stack<>();
    private static List<Bus> buses = new ArrayList<>();
    private static Map<String, Stack<String>> reservations = new HashMap<>();
    private static Queue<Customer> waitingQueue = new LinkedList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu options
            System.out.println("\nBus Reservation System Menu:");
            System.out.println("1. Customer Registration");
            System.out.println("2. Bus Registration");
            System.out.println("3. Search Buses");
            System.out.println("4. Reserve a Seat");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Request New Seat");
            System.out.println("7. Display Reservations");
            System.out.println("8. Display Registered Customers (Newest to Oldest)");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Execute the corresponding function based on user choice
            switch (choice) {
                case 1:
                    registerCustomer(scanner);
                    break;
                case 2:
                    registerBus(scanner);
                    break;
                case 3:
                    searchBuses(scanner);
                    break;
                case 4:
                    reserveSeat(scanner);
                    break;
                case 5:
                    cancelReservation(scanner);
                    break;
                case 6:
                    requestNewSeat(scanner);
                    break;
                case 7:
                    displayReservations();
                    break;
                case 8:
                    displayCustomersNewestToOldest();
                    break;
                case 9:
                    System.out.println("Exiting the system. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);

        scanner.close();
    }

 // Method to register a new customer
    private static void registerCustomer(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Mobile Number: ");
        String mobile = scanner.nextLine();
        System.out.print("Enter Email ID: ");
        String email = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Customer customer = new Customer(name, mobile, email, city, age);
        customerStack.push(customer);
        System.out.println("Customer registered successfully: " + customer);
    }

 // Method to register a new bus
       private static void registerBus(Scanner scanner) {
        System.out.print("Enter Bus Number: ");
        String busNumber = scanner.nextLine();
        System.out.print("Enter Total Seats: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Starting Point: ");
        String startingPoint = scanner.nextLine();
        System.out.print("Enter Ending Point: ");
        String endingPoint = scanner.nextLine();
        System.out.print("Enter Starting Time: ");
        String startingTime = scanner.nextLine();
        System.out.print("Enter Fare: ");
        double fare = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Bus bus = new Bus(busNumber, totalSeats, startingPoint, endingPoint, startingTime, fare);
        buses.add(bus);
        reservations.put(busNumber, new Stack<>());
        System.out.println("Bus registered successfully: " + bus);
    }

// Method to search for buses based on route
    private static void searchBuses(Scanner scanner) {
        System.out.print("Enter Starting Point: ");
        String start = scanner.nextLine();
        System.out.print("Enter Ending Point: ");
        String end = scanner.nextLine();

        boolean found = false;
        for (Bus bus : buses) {
            if (bus.startingPoint.equalsIgnoreCase(start) && bus.endingPoint.equalsIgnoreCase(end)) {
                System.out.println(bus);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No buses found for the given route.");
        }
    }

// Method to reserve a seat for a customer on a bus
    private static void reserveSeat(Scanner scanner) {
        System.out.print("Enter Bus Number: ");
        String busNumber = scanner.nextLine();
        System.out.print("Enter Your Name: ");
        String customerName = scanner.nextLine();

        if (reservations.containsKey(busNumber)) {
            Stack<String> busReservations = reservations.get(busNumber);
            // Check if there are available seats
             if (busReservations.size() > buses.stream().filter(bus -> bus.busNumber.equals(busNumber)).findFirst().get().totalSeats) {
                busReservations.push(customerName);
              System.out.println("Seat reserved successfully for " + customerName );
              System.out.println("Reservation Confirmed: Dear " + customerName + ", your seat has been successfully reserved on Bus " + busNumber + ". Have a safe journey!");
              
                     
            } else {
                // If no seats available, add to waiting queue 
                System.out.println("No available seats. Adding to waiting queue.");
                waitingQueue.add(new Customer(customerName, "", "", "", 0));
            }
        } else {
            System.out.println("Bus not found.");
        }
    }

// Method to cancel an existing reservation
    private static void cancelReservation(Scanner scanner) {
        System.out.print("Enter Bus Number: ");
        String busNumber = scanner.nextLine();

        if (reservations.containsKey(busNumber)) {
            Stack<String> busReservations = reservations.get(busNumber);
            if (!busReservations.isEmpty()) {
                String canceledCustomer = busReservations.pop();
                System.out.println("Reservation canceled for " + canceledCustomer  );
                // notify the customer about the cancellation
                  System.out.println("Notification: Your reservation for bus " + busNumber + " has been successfully canceled.");
                 
                 // If there are customers waiting, assign a seat to the next customer 
                if (!waitingQueue.isEmpty()) {
                    Customer nextCustomer = waitingQueue.poll();
                    busReservations.add(nextCustomer.name);
                    System.out.println("Seat assigned to waiting customer " + nextCustomer.name );
                    System.out.println("Notification: Dear " + nextCustomer.name + ", your reservation for Bus " + busNumber + " has been confirmed.");
                }
            } else {
                System.out.println("No reservations to cancel.");
            }
        } else {
            System.out.println("Bus not found.");
        }
    }

// Method to request a new seat (by adding the customer to the waiting queue)
    private static void requestNewSeat(Scanner scanner) {
        System.out.print("Enter Your Name: ");
        String customerName = scanner.nextLine();


        waitingQueue.add(new Customer(customerName, "", "", "", 0));
        System.out.println("Request added to the queue. Please wait for your turn.");
    }

// Method to display reservations for all buses
    private static void displayReservations() {
        for (Map.Entry<String, Stack<String>> entry : reservations.entrySet()) {
            System.out.println("Bus Number: " + entry.getKey() + " Reservations: " + entry.getValue());
        }
    }

    // Method to display registered customers in reverse order (newest first)
    private static void displayCustomersNewestToOldest() {
        if (customerStack.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            System.out.println("Displaying customers from newest to oldest:");
            while (!customerStack.isEmpty()) {
                System.out.println(customerStack.pop());
            }
        }
    }
}
