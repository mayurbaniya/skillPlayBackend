package com.gamingarena.controllers.admin;

import com.gamingarena.entities.games.Bgmi;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.services.admin.AdminBgmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/admin/bgmi")
public class BgmiController {


    @Autowired
    private AdminBgmiService adminBgmiService;

    @PostMapping("/create/adminId/{adminId}")
    public ResponseEntity<GlobalResponse> createMatch(
            @RequestBody Bgmi matchData,
            @PathVariable("adminId") String adminId
            ){

        GlobalResponse match = adminBgmiService.createMatch(matchData, Long.parseLong(adminId));

        return ResponseEntity.ok(match);
    }

}
