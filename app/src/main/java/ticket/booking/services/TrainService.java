package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private static final String TRAINS_PATH = "app/src/main/java/ticket/booking/localDB/trains.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Train> loadTrainsfromDB() throws IOException {
        File trainsFile = new File(TRAINS_PATH);
        // First load raw trains with seats as List<List<Integer>>
        List<Train> trains = objectMapper.readValue(trainsFile, new TypeReference<List<Train>>() {});

        // Convert seats from List<List<Integer>> to boolean[][]
        for (Train train : trains) {
            List<List<Integer>> seatsList = train.getSeatsAsList();
            if (seatsList != null && !seatsList.isEmpty()) {
                boolean[][] seatsArray = new boolean[seatsList.size()][];
                for (int i = 0; i < seatsList.size(); i++) {
                    List<Integer> row = seatsList.get(i);
                    seatsArray[i] = new boolean[row.size()];
                    for (int j = 0; j < row.size(); j++) {
                        // 1 means available (true), 0 means booked (false)
                        seatsArray[i][j] = (row.get(j) == 1);
                    }
                }
                train.setSeats(seatsArray);
            }
        }
        return trains;
    }

    public List<Train> searchTrains(String source, String destination) throws IOException {
        List<Train> trains = loadTrainsfromDB();
        List<Train> routeTrains = trains.stream()
                .filter(train -> train.getStations().contains(source) && train.getStations().contains(destination))
                .collect(Collectors.toList());

        if (routeTrains.isEmpty()) {
            System.out.println("No trains available for the route from " + source + " to " + destination + ".");
        } else {
            System.out.println(routeTrains.size() + " train(s) found for the route.");
        }
        return routeTrains;
    }

    public void availableSeats(Train train) {
        System.out.println("Seats availability for train: " + train.getTrainNo());
        boolean seatsAvailable = false;
        boolean[][] seats = train.getSeats();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                String seatStatus = seats[i][j] ? "Available" : "Booked";
                System.out.printf("Row %d, Seat %d: %s%n", i + 1, j + 1, seatStatus);
                if (seats[i][j]) seatsAvailable = true;
            }
        }
        if (!seatsAvailable) {
            System.out.println("No seats available on this train.");
        }
    }

    public boolean bookSeat(Train train, int row, int col) {
        // true means seat available, false means booked
        if (!train.getSeats()[row][col]) { // if seat is false (booked)
            System.out.println("Seat already booked.");
            return false;
        }
        // Mark seat as booked (false)
        train.getSeats()[row][col] = false;

        // TODO: Save train data to file here if persistence needed

        System.out.println("Seat booked successfully.");
        return true;
    }


}
