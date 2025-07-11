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
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDB/users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    private List<User> userList;

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        userList = loadUsersfromDB();
    }

    public UserBookingService() throws IOException {
        userList = loadUsersfromDB();
    }

    public List<User> loadUsersfromDB() throws IOException {
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Optional<User> loginUser(User inputUser) {
        return userList.stream()
                .filter(storedUser -> storedUser.getName().equalsIgnoreCase(inputUser.getName())
                        && UserServiceUtil.checkPassword(inputUser.getPassword(), storedUser.getHashedPassword()))
                .findFirst();
    }



    public Boolean signUpUser(User user1) {
        try {
            userList.add(user1);
            SaveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    private void SaveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    public void cancelBooking(Ticket ticket1, User user1) {
        user1.getTicketsBooked().removeIf(ticket -> ticket.getTicketID().equals(ticket1.getTicketID()));
        try {
            SaveUserListToFile();
        } catch (IOException e) {
            System.out.println("Failed to update file after cancelling ticket: " + e.getMessage());
        }
    }

    public void setUser(User user) {
        this.user= user;
    }
}
