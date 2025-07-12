package ticket.booking.entities;

import java.util.List;
import java.util.Map;

public class Train {
    private String trainID;
    private String trainNo;

    // This is the raw seats data loaded from JSON as List<List<Integer>>
    private List<List<Integer>> seatsAsList;

    // This is the actual seats array used internally (not serialized directly)
    private boolean[][] seats;

    private Map<String, String> stationTimes;
    private List<String> stations;

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeatsAsList() {
        return seatsAsList;
    }

    public void setSeatsAsList(List<List<Integer>> seatsAsList) {
        this.seatsAsList = seatsAsList;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public void setSeats(boolean[][] seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
