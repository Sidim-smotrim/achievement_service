package faang.school.achievement.handler.TaskCompleted;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.TaskCompletedEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public abstract class TaskCompletedHandler implements EventHandler<TaskCompletedEvent> {
    protected AchievementCache achievementCache;
    protected AchievementService achievementService;

    @Autowired
    public void setAchievementCache(AchievementCache achievementCache) {
        this.achievementCache = achievementCache;
    }

    @Autowired
    public void setAchievementService(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @Override
    public Class<?> supportsEventType() {
        return TaskCompletedEvent.class;
    }

    protected void processAchievement(Long userId, String achievementName) {
        Achievement achievement = achievementCache.get(achievementName);
        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievement.getId());
        if (!achievementService.hasAchievement(userId, achievement.getId())) {
            achievementService.createProgressIfNecessary(userId, achievement.getId());
            achievementService.updateProgress(userId, achievement.getId());
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(achievement, userId);
            }
        }
    }
}