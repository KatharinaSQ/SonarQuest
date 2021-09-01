package com.viadee.sonarquest.services;

import com.viadee.sonarquest.entities.*;
import com.viadee.sonarquest.repositories.LeaderBoardRepository;
import com.viadee.sonarquest.repositories.RaidLeaderboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeaderBoardService {

        private static final Logger LOGGER = LoggerFactory.getLogger(com.viadee.sonarquest.services.LeaderBoardService.class);
        @Autowired
        private LeaderBoardRepository leaderBoardRepository;

        /**
         * Update raid leader board with task reward
         * @param task
         */
        public void updateLeaderboard(final Task task) {
            if (task == null || task.getParticipation() == null)
                return;

            final Participation participation = task.getParticipation();
            final User user = participation.getUser();
            final Quest quest = task.getQuest();
            final Raid raid = quest.getRaid();
            final World world = user.getCurrentWorld();

            if (raid == null) // Task is not in raid!
                return;

            updateLeaderboardScore(user, world, task.getGold(), task.getXp());
        }

        /**
         * Update raid leader board with quest reward
         * @param quest
         */
        public void updateLeaderboard(final Quest quest) {
            if (quest == null)
                return;

            final List<Participation> participations = quest.getParticipations();
            for (Participation participation : participations) {
                final User user = participation.getUser();
                final Raid raid = quest.getRaid();
                final World world = user.getCurrentWorld();
                if (raid == null) // Quest is not in raid!
                    return;

                updateLeaderboardScore(user, world, quest.getGold(), quest.getXp());
            }
        }

        /**
         * Create new or update RaidLeaderboard
         * Adding score points (= Gold, XP)
         *
         * @param user
         * @param world
         * @param reward (= Gold, XP)
         * @return
         */
        @Transactional
        public Leaderboard updateLeaderboardScore(final User user, final World world, Long gold, Long xp) {
            Leaderboard board = leaderBoardRepository.findTopByWorldAndUser(world, user);
            if (board == null)
                board = new Leaderboard(user, world);

            board.setScoreDay(Date.valueOf(LocalDate.now()));
            board.addScoreGold(gold);
            board.addScoreXp(xp);

            LOGGER.info("Update LeaderboardScore - userID {} with {} gold and {} xp", user.getId(), gold, xp);

            return leaderBoardRepository.save(board);
        }
    }

