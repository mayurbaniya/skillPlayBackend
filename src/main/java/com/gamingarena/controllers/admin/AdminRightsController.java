package com.gamingarena.controllers.admin;


import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.services.admin.AdminRightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@CrossOrigin
public class AdminRightsController {


    @Autowired
    private AdminRightsService adminRightsService;

    @PostMapping("/setcoins")
    public ResponseEntity<GlobalResponse> setUserCoins(
            @RequestBody Map<String, String> map
    ){

         return ResponseEntity.ok(adminRightsService.manageUserCoins(
                Long.parseLong(map.get("userId")),
                Integer.parseInt(map.get("coins")))
        );

    }

    @PostMapping("/changeuserstatus")
    public ResponseEntity<GlobalResponse> changeUserStatus(
            @RequestBody Map<String, String> map
    ){

        return ResponseEntity.ok( adminRightsService.editUserStatus(
                Long.parseLong(map.get("userId")),
                Integer.parseInt(map.get("status")))
        );

    }


    @PostMapping("/changematchstatus")
    public ResponseEntity<GlobalResponse> changeMatchStatus(
            @RequestBody Map<String, String> map
    ){

        return ResponseEntity.ok( adminRightsService.editUserStatus(
                Long.parseLong(map.get("matchId")),
                Integer.parseInt(map.get("status")))
        );

    }


}
