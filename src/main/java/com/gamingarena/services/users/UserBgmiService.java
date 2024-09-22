package com.gamingarena.services.users;

import com.gamingarena.dto.BgmiDto;
import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.entities.games.Bgmi;
import com.gamingarena.payload.ActivityType;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.BgmiRepository;
import com.gamingarena.repositories.UserRepository;
import com.gamingarena.services.activities.UserActivityService;
import com.gamingarena.utils.Commons;
import com.gamingarena.utils.CustomMappingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserBgmiService {


    @Autowired
    private BgmiRepository bgmiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomMappingService customMappingService;

    public GlobalResponse joinMatch(String email, String matchId) {

        Bgmi match = bgmiRepository.findById(Long.parseLong(matchId)).orElse(null);
        Users user = userRepository.findByEmail(email);

        LocalDateTime now = LocalDateTime.now();

        if (match != null) {
            if (user != null) {
                if (user.getBgmiUsername() == null) {
                    return GlobalResponse.builder()
                            .msg("Please set your BGMI IGN first!")
                            .status(RespStatusEnum.GAME_ID_NOT_SET)
                            .build();
                }
                if (user.getBgmiData() != null && user.getBgmiData().contains(match)) {
                    return GlobalResponse.builder()
                            .msg("You've already joined this match!")
                            .status(RespStatusEnum.ALREADY_JOINED)
                            .build();
                }
                if (0 == match.getStatus()) {
                    return GlobalResponse.builder()
                            .msg("Match is already finished!")
                            .status(RespStatusEnum.FINISHED)
                            .build();
                }
                if (99 == match.getStatus()) {
                    return GlobalResponse.builder()
                            .msg("Match Cancelled!")
                            .status(RespStatusEnum.CANCELLED)
                            .build();
                }
                if (2 == match.getStatus()) {
                    return GlobalResponse.builder()
                            .msg("Ongoing Match!")
                            .status(RespStatusEnum.ONGOING)
                            .build();
                } else {
                    if (Duration.between(now, match.getStartTime()).toMinutes() >= 15) {
                        if (user.getStatus() != 99) {
                            if (user.getCoins() >= match.getEntryFee()) {

                                // Subtracting money
                                int remCoins = user.getCoins() - match.getEntryFee();
                                int coinsbefore = user.getCoins();
                                match.getParticipants().add(user);
                                user.setCoins(remCoins);
                                Map<String, Integer> m = new HashMap<>();
                                m.put("BEFORE", coinsbefore);
                                m.put("AFTER", remCoins);

                                try {
                                    userRepository.save(user);
                                    Bgmi matchData = bgmiRepository.save(match);

                                    userActivityService.
                                            joinedMatchActivity(
                                                    ActivityType.BGMI_MATCH_JOINED,
                                                    matchData.getId(),
                                                    ActivityType.JOINED_GAME,
                                                    Commons.userJoined,
                                                    new Date(),
                                                    user,
                                                    m.toString(),
                                                    remCoins);

                                    BgmiDto matchDTO = customMappingService.mapToBgmiDto(matchData);

                                    return GlobalResponse.builder()
                                            .msg("Match Joined!")
                                            .status(RespStatusEnum.SUCCESS)
                                            .data(matchDTO) // Return BgmiDTO
                                            .build();

                                } catch (Exception e) {
                                    return GlobalResponse.builder()
                                            .msg("Something went wrong!")
                                            .status(RespStatusEnum.ERROR)
                                            .data(e.getMessage())
                                            .build();
                                }

                            } else {
                                return GlobalResponse.builder()
                                        .msg("You don't have enough money!")
                                        .status(RespStatusEnum.NOT_ENOUGH_MONEY)
                                        .build();
                            }

                        } else {
                            return GlobalResponse.builder()
                                    .msg("You're banned!")
                                    .status(RespStatusEnum.BANNED)
                                    .build();
                        }

                    } else {
                        return GlobalResponse.builder()
                                .msg("Joining closed")
                                .status(RespStatusEnum.CLOSED)
                                .build();
                    }
                }

            } else {
                return GlobalResponse.builder()
                        .msg("Match not found!")
                        .status(RespStatusEnum.MATCH_NOT_FOUND)
                        .build();
            }
        } else {
            return GlobalResponse.builder()
                    .msg("User not found!")
                    .status(RespStatusEnum.USER_NOT_FOUND)
                    .build();
        }
    }


    public GlobalResponse getMatches() {
        try {
            LocalDateTime today = LocalDateTime.now();

            List<Bgmi> allMatches = bgmiRepository.findAll();


            // Mapping using ModelMapper
            List<BgmiDto> finishedMatches = allMatches.stream()
                    .filter(match -> match.getStatus() == 0)
                    .map(match -> customMappingService.mapToBgmiDto(match))
                    .collect(Collectors.toList());

            List<BgmiDto> activeMatches = allMatches.stream()
                    .filter(match -> match.getStatus() == 1 && match.getStartTime().toLocalDate().isEqual(today.toLocalDate()))
                    .map(match -> customMappingService.mapToBgmiDto(match))
                    .collect(Collectors.toList());

            List<BgmiDto> upcomingMatches = allMatches.stream()
                    .filter(match -> match.getStatus() == 1 && !match.getStartTime().toLocalDate().isEqual(today.toLocalDate()))
                    .map(match ->customMappingService.mapToBgmiDto(match))
                    .collect(Collectors.toList());

            List<BgmiDto> cancelledMatches = allMatches.stream()
                    .filter(match -> match.getStatus() == 99)
                    .map(match -> customMappingService.mapToBgmiDto(match))
                    .collect(Collectors.toList());

            if (finishedMatches.isEmpty() && activeMatches.isEmpty() && upcomingMatches.isEmpty() && cancelledMatches.isEmpty()) {
                return GlobalResponse.builder()
                        .msg("No matches found")
                        .status(RespStatusEnum.EMPTY)
                        .build();
            }

            Map<String, List<BgmiDto>> responseMap = new HashMap<>();
            responseMap.put("finished", finishedMatches);
            responseMap.put("active", activeMatches);
            responseMap.put("upcoming", upcomingMatches);
            responseMap.put("cancelled", cancelledMatches);

            return GlobalResponse.builder()
                    .msg("Matches found")
                    .status(RespStatusEnum.SUCCESS)
                    .data(responseMap) // Return the map of categorized matches
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return GlobalResponse.builder()
                    .msg("Something went wrong! Error occurred while fetching matches")
                    .status(RespStatusEnum.ERROR)
                    .err(e.getMessage())
                    .build();
        }
    }


    public GlobalResponse updateBGMIUsername(long userID, String bgmiUserName) {

        Optional<Users> byId = userRepository.findById(userID);

        if (byId.isPresent()) {
            Users user = byId.get();
            UserDto userDto = modelMapper.map(user, UserDto.class);

            user.setBgmiUsername(bgmiUserName);
            userRepository.save(user);

            userActivityService.otherActivity(
                    ActivityType.UPDATE_PROFILE,
                    "UPDATED BGMI USERNAME",
                    new Date(),
                    user,
                    "USER " + user.getUsername() + " UPDATED BGMI USERNAME TO " + user.getBgmiUsername(),
                    user.getCoins()
            );

            return GlobalResponse.builder()
                    .msg("BGMI USERNAME UPDATED")
                    .data(userDto)
                    .status(RespStatusEnum.SUCCESS)
                    .build();
        } else {
            return GlobalResponse.builder()
                    .msg("FAILED TO UPDATE BGMI USERNAME, USER NOT FOUND")
                    .status(RespStatusEnum.FAILED)
                    .build();
        }
    }



}
