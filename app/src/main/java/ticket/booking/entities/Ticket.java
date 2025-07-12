package ticket.booking.entities;

import java.util.Date;

public class Ticket {
    private String ticketID;
    @Override
    public String toString() {
        return "TicketID: " + ticketID +
                ", UserID: " + userID +
                ", Source: " + source +
                ", Destination: " + destination +
                ", DateOfTravel: " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateOfTravel) +
                ", TrainNo: " + (train != null ? train.getTrainNo() : "N/A");
    }
    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    private String userID;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private Train train;

}
