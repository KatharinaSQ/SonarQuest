package com.viadee.sonarquest.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viadee.sonarquest.constants.AdventureState;
import com.viadee.sonarquest.constants.QuestState;
import com.viadee.sonarquest.constants.RaidState;
import com.viadee.sonarquest.constants.SkillType;
import com.viadee.sonarquest.entities.Adventure;
import com.viadee.sonarquest.entities.Artefact;
import com.viadee.sonarquest.entities.Participation;
import com.viadee.sonarquest.entities.Quest;
import com.viadee.sonarquest.entities.Raid;
import com.viadee.sonarquest.entities.RaidLeaderboard;
import com.viadee.sonarquest.entities.Skill;
import com.viadee.sonarquest.entities.Task;
import com.viadee.sonarquest.entities.User;
import com.viadee.sonarquest.entities.World;
import com.viadee.sonarquest.interfaces.Reward;
import com.viadee.sonarquest.interfaces.UserGratification;

@Service
public class GratificationService implements UserGratification {

    private static final Logger LOGGER = LoggerFactory.getLogger(GratificationService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LevelService levelService;
    
    @Autowired
	private RaidLeaderboardService raidLeaderboardService;

    @Autowired
    private LeaderBoardService dashboardLeaderboardService;
    
    @Override
    @Transactional
    public synchronized void rewardUserForSolvingTask(final Task task) {
        LOGGER.debug("Task ID {} has changed the status from OPEN to SOLVED, rewarding users...", task.getId());
        final Participation participation = task.getParticipation();
        if (participation != null) {
            final User user = participation.getUser();
            LOGGER.info("Task {} solved - Rewarding userID {} with {} gold and {} xp", 
                    task.getKey(), user.getId(), task.getGold(), task.getXp());
            user.addXp(task.getXp());
            user.addGold(task.getGold());
            addSkillReward(user);
            user.setLevel(levelService.getLevelByUserXp(user.getXp()));
            userService.save(user);
            
            // Update LeaderBoard for solving task
            raidLeaderboardService.updateLeaderboardScore(user, task.getQuest().getRaid(), task.getGold(), task.getXp());
            dashboardLeaderboardService.updateLeaderboardScore(user, user.getCurrentWorld(),task.getGold(), task.getXp());
        } else {
            LOGGER.info("No SQUser participations found for task {}, so no rewards are paid out", task.getKey());
        }
    }

    private void rewardUserForSolvingAdventure(final User user, final Adventure adventure) {
        if (adventure.getStatus() != AdventureState.SOLVED) {
            LOGGER.info("Adventure {} solved - Rewarding userID {} with {} gold and {} xp", 
                    adventure.getId(), user.getId(), adventure.getGold(), adventure.getXp());
            user.addGold(adventure.getGold());
            user.addXp(adventure.getXp());
            user.setLevel(levelService.getLevelByUserXp(user.getXp()));
            userService.save(user);
        } else {
            LOGGER.warn("Adventure with ID {} is called for rewarding but is already solved. No rewards will be paid out.",
                    adventure.getId());
        }
    }

    @Override
    @Transactional
    public void rewardUsersForSolvingQuest(final Quest quest) {
        if (quest.getStatus() != QuestState.SOLVED) {
            final List<Participation> participations = quest.getParticipations();
            // First, find all Users only once that participated
            Set<User> usersThatTookPart = new HashSet<>();
            for (Participation participation : participations) {
                usersThatTookPart.add(participation.getUser());
            }
            LOGGER.info("A Quest has been solved by the following user IDs: {} and now the quest reward will be paid out to everyone.",
                    (Object) usersThatTookPart.stream().map(User::getId).toArray());
            // Now, reward them
            for (User user : usersThatTookPart) {
                rewardQuestParticipation(quest, user);
                
                // Update LeaderBoard for solving quest
                raidLeaderboardService.updateLeaderboardScore(user, quest.getRaid(), quest.getGold(), quest.getXp());
                dashboardLeaderboardService.updateLeaderboardScore(user, user.getCurrentWorld(),quest.getGold(), quest.getXp());
            }
        } else {
            LOGGER.warn("Quest with ID {} is called for rewarding but is already solved. No rewards will be paid out.",
                    quest.getId());
        }
    }

    @Override
    public void rewardUsersForSolvingAdventure(final Adventure adventure) {
        final List<User> users = adventure.getUsers();
        users.forEach(user -> rewardUserForSolvingAdventure(user, adventure));
    }
    
    @Override
    public void rewardUsersForSolvingRaid(final Raid raid) {
    	List<RaidLeaderboard> leaderBoardList = raid.getRaidLeaderboadList();
    	Collections.sort(leaderBoardList);
    	
    	long amountOfXP = leaderBoardList.stream().mapToLong(RaidLeaderboard::getScoreXp).sum();
    	
    	for (RaidLeaderboard raidLeaderboard : leaderBoardList) {
    		long percentage = raidLeaderboard.getScoreXp() / amountOfXP;
    		rewardUserForSolvingRaid(raidLeaderboard.getUser(), raid, percentage);
    	}
    }
    
    private void rewardUserForSolvingRaid(final User user, final Raid raid, long percentage) {
        if (raid.getStatus() != RaidState.SOLVED) {
            LOGGER.info("Raid {} solved - Rewarding userID {} with {} gold and {} xp", 
                    raid.getId(), user.getId(), raid.getGold(), raid.getXp());
            long gold = raid.getGold() * percentage;
            long xp = raid.getXp() * percentage;
            user.addGold(gold);
            user.addXp(xp);
            user.setLevel(levelService.getLevelByUserXp(user.getXp()));
            userService.save(user);
            
            raidLeaderboardService.updateLeaderboardScore(user, raid, gold, xp);
            dashboardLeaderboardService.updateLeaderboardScore(user, user.getCurrentWorld(),gold, xp);
        } else {
            LOGGER.warn("Raid with ID {} is called for rewarding but is already solved. No rewards will be paid out.",
                    raid.getId());
        }
    }

    private void rewardQuestParticipation(Quest quest, User user) {
        LOGGER.info("Rewarding participation in solving quest {} - Rewarding userID {} with {} gold and {} xp",
                quest.getId(), user.getId(), quest.getGold(), quest.getXp());
        user.addGold(quest.getGold());
        user.addXp(quest.getXp());
        user.setLevel(levelService.getLevelByUserXp(user.getXp()));
        userService.save(user);
    }

    private User addSkillReward(final User user) {
        final User rewardedUser = user;
        final List<Skill> avatarClassSkills = rewardedUser.getAvatarClass().getSkills();
        final List<Skill> artefactSkills = rewardedUser.getArtefacts().stream()
                .map(Artefact::getSkills)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        final List<Skill> totalSkills = new ArrayList<>();
        totalSkills.addAll(avatarClassSkills);
        totalSkills.addAll(artefactSkills);
        final Long extraGold = totalSkills.stream()
                .filter(skill -> skill.getType().equals(SkillType.GOLD))
                .mapToLong(Skill::getValue)
                .sum();
        final Long extraXP = totalSkills.stream()
                .filter(skill -> skill.getType().equals(SkillType.XP))
                .mapToLong(Skill::getValue)
                .sum();
        LOGGER.info("Adding skill rewards to user ID {} - extra gold {} and extra xp {}", 
                user.getId(), extraGold, extraXP);
        rewardedUser.addGold(extraGold);
        rewardedUser.addXp(extraXP);
        return rewardedUser;
    }

	@Override
	public void rewardUsersForPassedQualityGate(final World world, final Reward reward) {
		LOGGER.info("Rewarding all user in world {} with {} gold and {} xp - for passed quality gate!",
				world.getId(), reward.getGold(), reward.getXp());
		
		List<User> userList = userService.findByWorld(world);
		userList.forEach(user -> {
			user.addGold(reward.getGold());
			user.addXp(reward.getXp());
			user.setLevel(levelService.getLevelByUserXp(user.getXp()));
			userService.save(user);
		});
	}

}
