package faang.school.achievement.handler.mentorship;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.MentorshipStartEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SenseiAchievementEventHandlerTest {
    @InjectMocks
    private SenseiAchievementEventHandler senseiAchievementEventHandler;
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementService achievementService;
    private Achievement achievement;
    private AchievementProgress progress;
    private MentorshipStartEventDto mentorshipStartEventDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(senseiAchievementEventHandler, "achievementTitle", "Achievement-Sensei");

        mentorshipStartEventDto = MentorshipStartEventDto.builder()
                .menteeId(1L)
                .mentorId(2L)
                .build();

        achievement = Achievement.builder()
                .id(1L)
                .title("Achievement-Sensei")
                .description("Description")
                .rarity(Rarity.UNCOMMON)
                .points(3)
                .build();

        progress = AchievementProgress.builder()
                .userId(1L)
                .achievement(achievement)
                .currentPoints(2)
                .build();

        when(achievementCache.get("Achievement-Sensei")).thenReturn(achievement);
        when(achievementService.userHasAchievement(2L, 1L)).thenReturn(false);
        when(achievementService.getProgress(2L, 1L)).thenReturn(progress);
    }

    @Test
    void getAchievementTitle_shouldReturnSkillMaster() {
        assertEquals("Achievement-Sensei", senseiAchievementEventHandler.getAchievementTitle());
    }

    @Test
    void handle_shouldInvokeAchievementCacheGetMethod() {
        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementCache).get("Achievement-Sensei");
    }

    @Test
    void handle_shouldStopExecuting() {
        when(achievementService.userHasAchievement(2L, 1L)).thenReturn(true);

        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementService).userHasAchievement(2L, 1L);
        verifyNoMoreInteractions(achievementService);
    }

    @Test
    void handle_shouldInvokeAchievementServiceCreateProgressIfNecessaryMethod() {
        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementService).createProgressIfNecessary(2L, 1L);
    }

    @Test
    void handle_shouldInvokeAchievementServiceGetProgressMethod() {
        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementService).getProgress(2L, 1L);
    }

    @Test
    void handle_shouldInvokeAchievementServiceIncrementProgressMethod() {
        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementService).incrementProgress(progress);
    }

    @Test
    void handle_shouldInvokeAchievementServiceGiveAchievementMethod() {
        progress.setCurrentPoints(3);

        senseiAchievementEventHandler.handle(mentorshipStartEventDto);
        verify(achievementService).giveAchievement(2L, achievement);
    }
}