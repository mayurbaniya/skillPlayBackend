package com.gamingarena.repositories;

import com.gamingarena.entities.activities.UserActivities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityRepo extends JpaRepository<UserActivities, Integer> {


    List<UserActivities> findByUserId(int userId);

}
