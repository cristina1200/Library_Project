package mapper;


import model.User;
import model.Role;
import model.builder.UserBuilder;
import view.model.BookDTO;
import view.model.UserDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.UserDTOBuilder;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO convertUserToUserDTO(User user){
        String roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.joining(", "));
        return new UserDTOBuilder()
                .setUsername(user.getUsername())
                .setRole(roles)
                 .build();  }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users){
        return users.parallelStream().map(UserMapper::convertUserToUserDTO).collect(Collectors.toList());
    }
    public static User convertUserDTOToUser(UserDTO userDTO) {

        String username=userDTO.getUsername();
        return new UserBuilder().setUsername(username).build();
    }
}