package ticket.booking;

import ticket.booking.entities.User;
import ticket.booking.entities.Train;
import ticket.booking.services.UserBookingService;
import ticket.booking.services.TrainService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scan = new Scanner(System.in);
        int option = 0;

        UserBookingService userBookingService;
        TrainService trainService = new TrainService();
        Train selectedTrain = null;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            System.out.println("Failed to initialize UserBookingService: " + e.getMessage());
            return;
        }

        while (option != 7) {
            System.out.println("\nChoose option:");
            System.out.println("1. SignUp");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel My Booking");
            System.out.println("7. Exit the app");

            if (!scan.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scan.next(); // discard invalid input
                continue;
            }

            option = scan.nextInt();

            switch (option) {
                case 1: {
                    System.out.println("Enter a Username:");
                    String usernameSignUp = scan.next();
                    System.out.println("Create a Password:");
                    String passwordSignUp = scan.next();
                    User signUpUser = new User(usernameSignUp, passwordSignUp,
                            UserServiceUtil.hashPassword(passwordSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUpUser(signUpUser);
                    break;
                }

                case 2: {
                    System.out.println("Enter your Username:");
                    String usernameLogin = scan.next();
                    System.out.println("Enter your Password:");
                    String passwordLogin = scan.next();
                    User inputUser = new User(usernameLogin, passwordLogin, "", null, "");
                    try {
                        Optional<User> loggedInUser = userBookingService.loginUser(inputUser);
                        if (loggedInUser.isPresent()) {
                            System.out.println("Login successful!");
                            userBookingService.setUser(loggedInUser.get()); // Add setUser method in service
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } catch (Exception e) {
                        System.out.println("Login failed: " + e.getMessage());
                    }
                    break;
                }

                case 3: {
                    userBookingService.fetchBooking();
                    break;
                }

                case 4: {
                    System.out.println("Enter Source:");
                    String sourceStation = scan.next();
                    System.out.println("Enter Destination:");
                    String destinationStation = scan.next();
                    try {
                        List<Train> availableTrains = trainService.searchTrains(sourceStation, destinationStation);
                        if (availableTrains.isEmpty()) {
                            System.out.println("No trains found for the given route.");
                            selectedTrain = null;
                        } else {
                            System.out.println("Available trains:");
                            for (int i = 0; i < availableTrains.size(); i++) {
                                System.out.println((i + 1) + ". " + availableTrains.get(i));
                            }
                            System.out.println("Enter train number to select:");
                            int trainChoice = scan.nextInt() - 1;
                            if (trainChoice >= 0 && trainChoice < availableTrains.size()) {
                                selectedTrain = availableTrains.get(trainChoice);
                                System.out.println("Selected train: " + selectedTrain);
                            } else {
                                System.out.println("Invalid train choice.");
                                selectedTrain = null;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error searching trains: " + e.getMessage());
                        selectedTrain = null;
                    }
                    break;
                }

                case 5: {
                    if (selectedTrain == null) {
                        System.out.println("Please search and select a train before booking a seat.");
                        break;
                    }
                    System.out.println("Enter Row number (1–10):");
                    int row = scan.nextInt() - 1;
                    System.out.println("Enter Seat number (1–4):");
                    int col = scan.nextInt() - 1;

                    if (row < 0 || row >= 10 || col < 0 || col >= 4) {
                        System.out.println("Invalid seat selection. Please try again.");
                        break;
                    }
                    trainService.bookSeat(selectedTrain, row, col);

                    // Optionally save updated trains to DB here if implemented
                    break;
                }

                case 6: {
                    System.out.println("Cancel booking feature not implemented yet.");
                    break;
                }

                case 7: {
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                }

                default: {
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
                    break;                }
            }
        }
    }
}
