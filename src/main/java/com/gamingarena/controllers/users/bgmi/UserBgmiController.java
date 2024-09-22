package com.gamingarena.controllers.users.bgmi;

import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.services.users.UserBgmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("user/bgmi/")
@CrossOrigin
public class UserBgmiController {

    @Autowired
    private UserBgmiService userBgmiService;


    @PostMapping("/join")
    public ResponseEntity<GlobalResponse> joinMatch(
            @RequestBody Map<String,String> map
            ){

        String email = map.get("email");
        String matchId = map.get("match_id");

        return  ResponseEntity.ok(userBgmiService.joinMatch(email, matchId));

    }

    @GetMapping("/matches")
    public ResponseEntity<GlobalResponse> getMatches(){

        return ResponseEntity.ok(userBgmiService.getMatches());
    }

    @PostMapping("/update-bgmi-username")
    public ResponseEntity<GlobalResponse> updateBGMIUsername(
            @RequestBody Map<String, String> userData
    ){

        String userID = userData.get("userID");
        String bgmiUsername = userData.get("BGMIUsername");

        return ResponseEntity
                .ok(userBgmiService.updateBGMIUsername(Long.parseLong(userID), bgmiUsername));
    }

}
