package com.gamingarena.repositories;

import com.gamingarena.entities.activities.AdminActivities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActivityRepo extends JpaRepository<AdminActivities, Long> {
}
