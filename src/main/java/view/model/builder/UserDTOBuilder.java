package view.model.builder;

import view.model.UserDTO;

public class UserDTOBuilder {
    private UserDTO userDTO;

    public UserDTOBuilder() {
        userDTO = new UserDTO();
    }

    public UserDTOBuilder setUsername(String username) {
        userDTO.setUsername(username);
        return this;
    }
    public UserDTOBuilder setRole(String role) {
        userDTO.setRole(role);
        return this;
    }
    public UserDTO build(){
        return userDTO;
    }
}
