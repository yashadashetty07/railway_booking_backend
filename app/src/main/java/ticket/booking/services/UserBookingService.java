package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private final User user;
    private final List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "../localDB/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1-> user1.getName().equalsIgnoreCase(user.getName())&& UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword())).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUpUser(User user1){
        try{
            userList.add(user1);
            SaveUserListToFile();
            return Boolean.TRUE;
        }
        catch (IOException e){
            return Boolean.FALSE;
        }
    }
    private void SaveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList);
    }
}
