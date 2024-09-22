package com.gamingarena.controllers.creator;

import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.services.creator.CreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creator")
public class CreatorController {


    @Autowired
    private CreatorService creatorService;


    @GetMapping("/fetch-all-admin-activities/creatorID={creatorID}/creatorPass={creatorPass}")
    private ResponseEntity<GlobalResponse> getAllAdminActivities(
            @PathVariable("creatorID") String id,
            @PathVariable("creatorPass") String pass
    ){

        return ResponseEntity.ok( creatorService.getAllAdminActivities(Long.parseLong(id), pass));

    }

}
