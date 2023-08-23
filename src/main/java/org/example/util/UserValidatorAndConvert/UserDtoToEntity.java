package org.example.util.UserValidatorAndConvert;

import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToEntity {

    public UserEntity convert(UserDTO dto) {
        UserEntity userEntity = new UserEntity();
        return getUserEntity(dto, userEntity);
    }

    public UserEntity convert(UserDTO dto,UserEntity userEntity) {
        return getUserEntity(dto, userEntity);
    }

    private UserEntity getUserEntity(UserDTO dto, UserEntity userEntity) {
        userEntity.setLogin(dto.getLogin());
        userEntity.setPass(dto.getPass());
        userEntity.setName(dto.getName());
        userEntity.setSurname(dto.getSurname());
        userEntity.setTelephone(dto.getTelephone());
        userEntity.setEmail(dto.getEmail());
        userEntity.setPath(dto.getPath());
        return userEntity;
    }

    public UserDTO convertToDto(UserEntity userEntity) {
        UserDTO userDTO=new UserDTO();
        userDTO.setLogin(userEntity.getLogin());
        userDTO.setPass(userEntity.getPass());
        userDTO.setName(userEntity.getName());
        userDTO.setSurname(userEntity.getSurname());
        userDTO.setTelephone(userEntity.getTelephone());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPath(userEntity.getPath());
        return userDTO;
    }


}
