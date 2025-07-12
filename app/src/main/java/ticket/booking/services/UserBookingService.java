package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDB/users.json";

    public UserBookingService() throws IOException {
        File usersFile = new File(USERS_PATH);
        userList = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {
        });
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        File usersFile = new File(USERS_PATH);
        userList = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {
        });
    }

    public boolean signUpUser(User newUser) {
        Optional<User> existingUser = userList.stream()
                .filter(u -> u.getName().equals(newUser.getName()))
                .findAny();

        if (existingUser.isPresent()) {
            System.out.println("SignUp Error: Username '" + newUser.getName() + "' already exists.");
            return false;
        }

        userList.add(newUser);
        try {
            saveUserListToFile();
            System.out.println("User '" + newUser.getName() + "' registered successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("SignUp Error: Unable to save user data - " + e.getMessage());
            return false;
        }
    }

    public Optional<User> loginUser(User inputUser) {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> user1.getName().equals(inputUser.getName()) &&
                        UserServiceUtil.checkPassword(inputUser.getPassword(), user1.getHashedPassword()))
                .findAny();

        if (foundUser.isPresent()) {
            System.out.println("Login successful for user: " + inputUser.getName());
        } else {
            System.out.println("Login failed: Invalid username or password.");
        }
        return foundUser;
    }

    public void fetchBooking() {
        if (user == null) {
            System.out.println("No user logged in. Please login first.");
            return;
        }
        if (user.getTicketsBooked() == null || user.getTicketsBooked().isEmpty()) {
            System.out.println("No bookings found for user: " + user.getName());
            return;
        }
        user.getTicketsBooked().forEach(ticket -> System.out.println(ticket.toString()));
    }

    public void cancelBooking(Ticket ticket, User user) {
        if (user == null) {
            System.out.println("No user logged in. Cancellation failed.");
            return;
        }
        boolean removed = user.getTicketsBooked().removeIf(t -> t.getTicketID().equals(ticket.getTicketID()));

        if (removed) {
            try {
                saveUserListToFile();
                System.out.println("Booking with TicketID " + ticket.getTicketID() + " cancelled successfully.");
            } catch (IOException e) {
                System.out.println("Error updating bookings after cancellation: " + e.getMessage());
            }
        } else {
            System.out.println("Cancellation failed: Booking not found.");
        }
    }

    public void bookTicket(Ticket ticket) {
        if (user == null) {
            System.out.println("No user logged in. Booking failed.");
            return;
        }

        user.getTicketsBooked().add(ticket);

        try {
            saveUserListToFile();
            System.out.println("Ticket booked successfully with TicketID: " + ticket.getTicketID());
        } catch (IOException e) {
            System.out.println("Booking failed: Unable to update user data - " + e.getMessage());
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
