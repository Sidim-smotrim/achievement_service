package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementEventDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.AchievementProgressMapperImpl;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.mapper.UserAchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementEventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementEventPublisher achievementEventPublisher;
    @Spy
    private AchievementMapper achievementMapper = new AchievementMapperImpl();
    @Spy
    private AchievementProgressMapper achievementProgressMapper = new AchievementProgressMapperImpl(achievementMapper);
    @Spy
    private UserAchievementMapper userAchievementMapper = new UserAchievementMapperImpl(achievementMapper);
    @InjectMocks
    private AchievementService achievementService;

    private Achievement achievement;
    private Achievement firstAchievement;
    private Achievement secondAchievement;

    private UserAchievement firstUserAchievement;
    private UserAchievement secondUserAchievement;

    private AchievementProgress firstProgress;
    private AchievementProgress secondProgress;

    private UserAchievementDto firstUserAchievementDto;
    private UserAchievementDto secondUserAchievementDto;

    private AchievementProgressDto firstDto;
    private AchievementProgressDto secondDto;

    private AchievementDto firstAchievementDto;
    private AchievementDto secondAchievementDto;

    private LocalDateTime currentTime;


    @BeforeEach
    void setUp() {
        UserAchievement first = UserAchievement.builder()
                .userId(1)
                .build();
        UserAchievement second = UserAchievement.builder()
                .userId(2)
                .build();
        achievement = Achievement.builder()
                .id(1)
                .userAchievements(List.of(first, second))
                .build();
        currentTime = LocalDateTime.now();
        firstAchievement = Achievement.builder()
                .id(1)
                .title("first title")
                .description("first description")
                .rarity(Rarity.COMMON)
                .points(100)
                .build();
        firstUserAchievement = UserAchievement.builder()
                .id(1)
                .achievement(firstAchievement)
                .userId(1)
                .createdAt(currentTime)
                .build();
        firstProgress = AchievementProgress.builder()
                .id(1)
                .achievement(firstAchievement)
                .userId(1)
                .currentPoints(35)
                .build();
        secondAchievement = Achievement.builder()
                .id(2)
                .title("second title")
                .description("second description")
                .rarity(Rarity.RARE)
                .points(200)
                .build();
        secondUserAchievement = UserAchievement.builder()
                .id(2)
                .achievement(secondAchievement)
                .userId(2)
                .createdAt(currentTime)
                .build();
        secondProgress = AchievementProgress.builder()
                .id(2)
                .achievement(secondAchievement)
                .userId(2)
                .currentPoints(55)
                .build();
        firstAchievementDto = AchievementDto.builder()
                .id(1)
                .title("first title")
                .description("first description")
                .rarity(Rarity.COMMON)
                .points(100)
                .build();
        firstUserAchievementDto = UserAchievementDto.builder()
                .id(1)
                .achievementDto(firstAchievementDto)
                .userId(1)
                .receivedAt(currentTime)
                .build();
        firstDto = AchievementProgressDto.builder()
                .id(1)
                .achievementDto(firstAchievementDto)
                .userId(1)
                .currentPoints(35)
                .build();
        secondAchievementDto = AchievementDto.builder()
                .id(2)
                .title("second title")
                .description("second description")
                .rarity(Rarity.RARE)
                .points(200)
                .build();
        secondUserAchievementDto = UserAchievementDto.builder()
                .id(2)
                .achievementDto(secondAchievementDto)
                .userId(2)
                .receivedAt(currentTime)
                .build();
        secondDto = AchievementProgressDto.builder()
                .id(2)
                .achievementDto(secondAchievementDto)
                .userId(2)
                .currentPoints(55)
                .build();
    }
//  private long id;
//    private String title;
//    private String description;
//    private Rarity rarity;
//    private long points;

    @Test
    void getAchievementInformationTest() {
        when(achievementRepository.findById(2L)).thenReturn(Optional.of(firstAchievement));

        AchievementDto result = achievementService.getAchievementInformation(2);

        assertEquals(firstAchievementDto, result);
    }

    @Test
    void getUserAchievementsTest() {
        when(userAchievementRepository.findByUserId(2)).thenReturn(List.of(firstUserAchievement, secondUserAchievement));

        List<UserAchievementDto> expected = List.of(firstUserAchievementDto, secondUserAchievementDto);

        List<UserAchievementDto> result = achievementService.getUserAchievements(2);

        assertEquals(expected, result);

        verify(userAchievementRepository).findByUserId(2);
    }

    @Test
    void getAllAchievementProgressByUserIdTest() {
        when(achievementProgressRepository.findByUserId(5)).thenReturn(List.of(firstProgress, secondProgress));

        List<AchievementProgressDto> expected = List.of(firstDto, secondDto);

        List<AchievementProgressDto> result = achievementService.getAllAchievementProgressByUserId(5);

        assertEquals(expected, result);

        verify(achievementProgressRepository).findByUserId(5);
    }

    @Test
    void getAchievementProgressByUserIdTest() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(2, 1))
                .thenReturn(Optional.of(firstProgress));

        AchievementProgressDto result = achievementService.getAchievementProgressByUserId(1, 2);

        assertEquals(firstDto, result);
    }

    @Test
    void giveAchievementTest() {
        achievementService.giveAchievement(achievement, 1);

        verify(userAchievementRepository).save(any(UserAchievement.class));
        verify(achievementEventPublisher).publishMessage(any(AchievementEventDto.class));
    }

    @Test
    void addAchievementToUserIfEnoughPointsTest() {
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .currentPoints(100)
                .build();

        achievementService.addAchievementToUserIfEnoughPoints(achievementProgress, achievement, 1);

        verify(userAchievementRepository).save(any(UserAchievement.class));
        verify(achievementEventPublisher).publishMessage(any(AchievementEventDto.class));
    }

    @Test
    void getUserProgressByAchievementAndUserIdFirstScenarioTest() {
        AchievementProgress expectedAchievementProgress = AchievementProgress.builder()
                .currentPoints(35)
                .build();

        when(achievementProgressRepository.findByUserIdAndAchievementId(1, 1))
                .thenReturn(Optional.of(expectedAchievementProgress));

        AchievementProgress result = achievementService.getUserProgressByAchievementAndUserId(achievement.getId(), 1);

        assertEquals(expectedAchievementProgress, result);
    }

    @Test
    void getUserProgressByAchievementAndUserIdSecondScenarioTest() {
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .currentPoints(0)
                .build();

        doReturn(Optional.empty()).doReturn(Optional.of(achievementProgress))
                .when(achievementProgressRepository).findByUserIdAndAchievementId(2, 1);

        AchievementProgress result = achievementService.getUserProgressByAchievementAndUserId(achievement.getId(), 2);

        assertEquals(achievementProgress, result);

        verify(achievementProgressRepository).createProgressIfNecessary(2, 1);
        verify(achievementProgressRepository, times(2)).findByUserIdAndAchievementId(2, 1);
    }

    @Test
    void hasAchievementTest() {
        boolean result = achievementService.hasAchievement(achievement, 2);

        assertTrue(result);
    }
}