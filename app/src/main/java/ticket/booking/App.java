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
            System.out.println("Error initializing User service: " + e.getMessage());
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
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scan.next();
                continue;
            }

            option = scan.nextInt();

            switch (option) {
                case 1: {
                    System.out.println("Enter Username:");
                    String usernameSignUp = scan.next();
                    System.out.println("Create Password:");
                    String passwordSignUp = scan.next();
                    User signUpUser = new User(usernameSignUp, passwordSignUp,
                            UserServiceUtil.hashPassword(passwordSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                    if (userBookingService.signUpUser(signUpUser)) {
                        System.out.println("SignUp successful! Please login.");
                    } else {
                        System.out.println("SignUp failed. Please try again.");
                    }
                    break;
                }

                case 2: {
                    System.out.println("Enter Username:");
                    String usernameLogin = scan.next();
                    System.out.println("Enter Password:");
                    String passwordLogin = scan.next();
                    User inputUser = new User(usernameLogin, passwordLogin, "", null, "");
                    try {
                        Optional<User> loggedInUser = userBookingService.loginUser(inputUser);
                        if (loggedInUser.isPresent()) {
                            System.out.println("Login successful! Welcome, " + loggedInUser.get().getName());
                            userBookingService.setUser(loggedInUser.get());
                        } else {
                            System.out.println("Login failed. Invalid username or password.");
                        }
                    } catch (Exception e) {
                        System.out.println("Login error: " + e.getMessage());
                    }
                    break;
                }

                case 3: {
                    if (userBookingService.getUser() == null) {
                        System.out.println("Please login first to view bookings.");
                        break;
                    }
                    List<?> bookings = userBookingService.getUser().getTicketsBooked();
                    if (bookings == null || bookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        System.out.println("Your bookings:");
                        userBookingService.fetchBooking();
                    }
                    break;
                }

                case 4:{
                    System.out.println("Enter Source Station:");
                    String sourceStation = scan.next();
                    System.out.println("Enter Destination Station:");
                    String destinationStation = scan.next();
                    try {
                        List<Train> availableTrains = trainService.searchTrains(sourceStation, destinationStation);
                        if (availableTrains.isEmpty()) {
                            System.out.println("No trains found from " + sourceStation + " to " + destinationStation + ".");
                        } else {
                            System.out.println("Available trains:");
                            for (int i = 0; i < availableTrains.size(); i++) {
                                System.out.println((i + 1) + ". " + availableTrains.get(i).getTrainNo());
                            }
                            System.out.println("Select train number to proceed:");
                            int trainChoice = scan.nextInt() - 1;
                            if (trainChoice >= 0 && trainChoice < availableTrains.size()) {
                                selectedTrain = availableTrains.get(trainChoice);
                                System.out.println("Selected train: " + selectedTrain.getTrainNo());
                            } else {
                                System.out.println("Invalid selection. No train selected.");
                                selectedTrain = null;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error fetching trains: " + e.getMessage());
                    }
                    break;
                }


                case 5: {
                    if (userBookingService.getUser() == null) {
                        System.out.println("Please login before booking.");
                        break;
                    }

                    if (selectedTrain == null) {
                        System.out.println("Please search and select a train before booking.");
                        break;
                    }

                    System.out.println("Enter Row number (1–10):");
                    int row = scan.nextInt() - 1;
                    System.out.println("Enter Seat number (1–4):");
                    int col = scan.nextInt() - 1;

                    if (row < 0 || row >= 10 || col < 0 || col >= 4) {
                        System.out.println("Invalid seat selection. Please choose Row 1–10 and Seat 1–4.");
                        break;
                    }

                    boolean booked = trainService.bookSeat(selectedTrain, row, col);
                    if (!booked) {
                        System.out.println("Seat already booked or booking failed.");
                        break;
                    }

                    System.out.println("Enter Source Station:");
                    String source = scan.next();
                    System.out.println("Enter Destination Station:");
                    String destination = scan.next();

                    System.out.println("Enter Travel Date (yyyy-MM-dd):");
                    String dateStr = scan.next();
                    Date dateOfTravel;
                    try {
                        dateOfTravel = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        break;
                    }

                    // ✅ Create and add the ticket
                    ticket.booking.entities.Ticket ticket = new ticket.booking.entities.Ticket();
                    ticket.setTicketID("TKT" + UUID.randomUUID().toString().substring(0, 5));
                    ticket.setUserID(userBookingService.getUser().getUserID());
                    ticket.setSource(source.toLowerCase());
                    ticket.setDestination(destination.toLowerCase());
                    ticket.setDateOfTravel(dateOfTravel);
                    ticket.setTrain(selectedTrain);

                    userBookingService.bookTicket(ticket);
                    break;
                }


                case 6: {
                    if (userBookingService.getUser() == null) {
                        System.out.println("Please login first to cancel bookings.");
                        break;
                    }
                    List<?> bookings = userBookingService.getUser().getTicketsBooked();
                    if (bookings == null || bookings.isEmpty()) {
                        System.out.println("You have no bookings to cancel.");
                    } else {
                        System.out.println("Your bookings:");
                        for (int i = 0; i < bookings.size(); i++) {
                            System.out.println((i + 1) + ". " + bookings.get(i).toString());
                        }
                        System.out.println("Enter booking number to cancel:");
                        int cancelChoice = scan.nextInt() - 1;
                        if (cancelChoice >= 0 && cancelChoice < bookings.size()) {
                            userBookingService.cancelBooking((ticket.booking.entities.Ticket) bookings.get(cancelChoice), userBookingService.getUser());
                            System.out.println("Booking cancelled successfully.");
                        } else {
                            System.out.println("Invalid selection. Cancellation aborted.");
                        }
                    }
                    break;
                }

                case 7: {
                    System.out.println("Exiting app. Thank you!");
                    System.exit(0);
                    break;
                }

                default: {
                    System.out.println("Invalid option. Please select 1 to 7.");
                }
            }
        }
        scan.close();
    }
}
