package com.gamingarena.config;

import com.gamingarena.dto.BgmiDto;
import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.entities.games.Bgmi;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurations {

    @Bean
    public ModelMapper mapper(){

        ModelMapper modelMapper =  new ModelMapper();

        // Customize mapping to avoid infinite recursion
        modelMapper.typeMap(Bgmi.class, BgmiDto.class).addMappings(mapper -> {
            mapper.skip(BgmiDto::setParticipants); // Prevent direct mapping
        });

        modelMapper.typeMap(Users.class, UserDto.class).addMappings(mapper -> {
            mapper.skip(UserDto::setBgmiData); // Prevent direct mapping
        });

        return  modelMapper;
    }
}
