package com.gamingarena.repositories;



import com.gamingarena.entities.games.Bgmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BgmiRepository extends JpaRepository<Bgmi, Long> {

    @Query("SELECT b FROM Bgmi b WHERE b.status = :status AND b.startTime <= :twentyMinutesBefore")
    List<Bgmi> findActiveMatchesBefore(@Param("status") int status, @Param("twentyMinutesBefore") LocalDateTime twentyMinutesBefore);



    List<Bgmi> findAllByStatus(int status);
}
