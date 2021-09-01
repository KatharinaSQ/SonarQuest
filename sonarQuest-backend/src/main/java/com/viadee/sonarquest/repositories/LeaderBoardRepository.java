package com.viadee.sonarquest.repositories;

import com.viadee.sonarquest.entities.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeaderBoardRepository extends CrudRepository<Leaderboard, Long> {

      Leaderboard findTopByWorldAndUser(World world, User user);
        List<Leaderboard> findByWorldIdOrderByScoreXp(Long world_id);
}
