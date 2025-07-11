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
    private Train train;

    public List<Train> loadTrainsfromDB() throws IOException {
        File trainList = new File(TRAINS_PATH);
        return objectMapper.readValue(trainList, new TypeReference<List<Train>>() {
        });
    }

    public List<Train> searchTrains(String source, String destination) throws IOException {
        return loadTrainsfromDB().stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        int sourceIndex = stationOrder.indexOf(source.trim().toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.trim().toLowerCase());

        return (sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex);
    }

    public void availableSeats(Train train) {
        System.out.println("Available seat matrix (0 = available, 1 = booked):");
        for (int i = 0; i < train.getSeats().size(); i++) {
            System.out.print("Row " + (i + 1) + ": ");
            for (int j = 0; j < train.getSeats().get(i).size(); j++) {
                System.out.print(train.getSeats().get(i).get(j) + " ");
            }
            System.out.println();
        }
    }


    public void bookSeat(Train train, int row, int col) {
        if (row < 0 || row >= train.getSeats().size() || col < 0 || col >= train.getSeats().get(0).size()) {
            System.out.println("Invalid seat selection. Row should be 1–10 and seat 1–4.");
            return;
        }

        if (train.getSeats().get(row).get(col) == 0) {
            train.getSeats().get(row).set(col, 1);
            System.out.println("Seat booked successfully at Row " + (row + 1) + ", Seat " + (col + 1));
        } else {
            System.out.println("Seat is already booked. Try another one.");
        }
    }
    public void saveTrainsToDB(List<Train> trains) throws IOException {
        objectMapper.writeValue(new File(TRAINS_PATH), trains);
    }


}
