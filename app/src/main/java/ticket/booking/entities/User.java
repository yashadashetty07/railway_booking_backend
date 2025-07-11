package ticket.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userID;

    public User (String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userID){
    this. name = name;
    this. password = password;
    this.hashedPassword=hashedPassword;
    this.ticketsBooked=ticketsBooked;
    this.userID = userID;
    }
    public User() {
        // Required by Jackson for deserialization
    }
        // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    // Display info for a single ticket
    public void getTicketInfo(Ticket ticket) {
        System.out.println(ticket);
    }

    // Print all tickets
    public void printTickets() {
        if (ticketsBooked != null) {
            for (Ticket ticket : ticketsBooked) {
                System.out.println(ticket);
            }
        } else {
            System.out.println("No tickets booked.");
        }
    }
}
