package com.gamingarena.services.scheduled_tasks;

import com.gamingarena.entities.games.Bgmi;
import com.gamingarena.repositories.BgmiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledUpdateMatchStatus {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateMatchStatus.class);

    @Autowired
    private BgmiRepository bgmiRepository;

    @Scheduled(fixedRate = 60000) // Every 10 minutes
    @Transactional
    public void updateMatchStatus() {
        logger.info("Scheduled task started at {}", LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyMinutesBefore = now.minusMinutes(20);
        logger.info("Time 20 minutes before: {}", twentyMinutesBefore);

        // Fetch only active matches with start time before now
        List<Bgmi> matches = bgmiRepository.findActiveMatchesBefore(1,twentyMinutesBefore);
        logger.error("Found {} matches to update", matches.size());

        for (Bgmi match : matches) {
            logger.info("fetched match time: {}", match.getStartTime());
            logger.info("Updating match id: {}", match.getId());
            match.setStatus(0); // Set status to 0 (finished)
        }

        bgmiRepository.saveAll(matches); // Batch save for efficiency
        logger.info("Scheduled task finished at {}", LocalDateTime.now());
    }
}
