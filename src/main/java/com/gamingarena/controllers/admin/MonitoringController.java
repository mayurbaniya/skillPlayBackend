package com.gamingarena.controllers.admin;

import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.services.admin.MonitoringServices;
import com.gamingarena.services.users.UserBgmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class MonitoringController {

    @Autowired
    private MonitoringServices monitoringServices;

    @Autowired
    private UserBgmiService userBgmiService;


    @GetMapping("/get-all-users")
    public ResponseEntity<GlobalResponse> getAllUsers() {
        return ResponseEntity.ok(monitoringServices.getAllUsers());
    }

    @GetMapping("/get-user-by-id/{id}")
    public ResponseEntity<GlobalResponse> getUserById(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(monitoringServices.getUserById(Long.parseLong(id)));
    }

    @GetMapping("/get-activity-by-id/{id}")
    public ResponseEntity<GlobalResponse> getActivityById(
            @PathVariable("id") String id
    ){
        return ResponseEntity.ok(
                monitoringServices.getUserActivityById(Integer.parseInt(id))
        );
    }

    @GetMapping("/get-all-activity")
    public ResponseEntity<GlobalResponse> getAllActivity(
    ){

        return ResponseEntity.ok(
                monitoringServices.getAllUserActivities()
        );
    }


}
