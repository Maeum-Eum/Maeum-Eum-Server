package com.five.Maeum_Eum;

import com.five.Maeum_Eum.dto.manager.CaregiverWithOverlapDto;
import com.five.Maeum_Eum.entity.CaregiverTime;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
public class CaregiverMatchingTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ManagerContactQueryDsl managerContactQueryDsl;

    @Test
    @DisplayName(value = "인지 지원 등급 매칭 테스트 : 요양사의 가능 등급에는 2가 포함 되어야 함")
    public void testElderRankMatchingTest(){

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for (int i = 1; i <= 20; i++) {

            Caregiver caregiver = Caregiver.builder()
                    .name("Caregiver " + i)
                    .address("Address " + i)
                    .hasCaregiverCertificate(false)
                    .isResumeRegistered(true)
                    .isJobOpen(false)
                    .jobState(Caregiver.JobState.MATCHED)
                    .phoneNumber("010-1234-5678")
                    .build();

            List<Integer> details = new ArrayList<>();

            int numRanks = random.nextInt(5) + 1; // 1~5개 등급
            for (int j = 0; j < numRanks; j++) { // 최대 5개 등급 넣기
                int rank = random.nextInt(7); // 0~6 범위의 랜덤 등급
                if (!details.contains(rank)) {
                    details.add(rank);
                }
            }

            Resume resume = Resume.builder()
                    .caregiver(caregiver)
                    .elderRank(details)
                    .build();

            caregiver.setResume(resume);
            em.persist(caregiver);
            em.persist(resume);
            em.flush();
        }

        int elderRank = 2;

        List<Caregiver> caregivers = managerContactQueryDsl.findQualifiedCaregivers(elderRank);

        Assertions.assertNotNull(caregivers);
        Assertions.assertFalse(caregivers.isEmpty());

        for (Caregiver c : caregivers) {
            System.out.println("[LOG] " + c.getName());

            for (Integer rank : c.getResume().getElderRank()) {
                System.out.println("[LOG] " + rank);
            }
        }
    }

    @Test
    @DisplayName(value = "시간대 매칭 테스트")
    public void testFindingCaregiverByMatchingTimeSlot(){

        for (int i = 1; i <= 20; i++) {
            Caregiver caregiver = Caregiver.builder()
                    .name("Caregiver " + i)
                    .address("Address " + i)
                    .hasCaregiverCertificate(false)
                    .isResumeRegistered(false)
                    .isJobOpen(false)
                    .jobState(Caregiver.JobState.MATCHED)
                    .phoneNumber("010-1234-5678")
                    .build();

            em.persist(caregiver);

            DayOfWeek workDay = DayOfWeek.TUE;
            LocalTime startTime = LocalTime.of(8 + (i % 5), 0); // 08:00 ~ 12:00 사이
            LocalTime endTime = startTime.plusHours(3);

            System.out.println("[LOG] " + caregiver.getName() + " " + startTime + " " + endTime);

            CaregiverTime ct = CaregiverTime.builder()
                    .caregiver(caregiver)
                    .workDay(workDay)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

            System.out.println("[LOG] " + caregiver.getName() + " " + ct.getStartTime() + " " + ct.getEndTime());
            em.persist(ct);
        }
        em.flush();
        em.clear();

        // 어르신의 요구 시간대를 담은 ServiceSlot 리스트 생성
        ServiceSlot slot1 = ServiceSlot.builder()
                .serviceSlotDay(DayOfWeek.TUE)
                .serviceSlotStart(LocalTime.of(8,30))
                .serviceSlotEnd(LocalTime.of(11, 0))
                .build();

        List<ServiceSlot> serviceSlots = new ArrayList<>();
        serviceSlots.add(slot1);

        // 쿼리 메서드 호출
        List<CaregiverWithOverlapDto> result = managerContactQueryDsl.findCaregiverByMatchingTimeSlot(serviceSlots);

        // 결과 확인 (예시로 결과가 비어있지 않은지, 예상 순서대로 정렬되었는지 확인)
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(10, result.size());

        for (CaregiverWithOverlapDto dto : result) {
            Caregiver caregiver = dto.getCaregiver();
            Integer totalOverlap = dto.getTotalOverlap();

            System.out.println("Matched Caregiver: " + caregiver.getCaregiverTime().get(0).getStartTime() + " 겹치는 시간 : " + totalOverlap
            );
        }

        List<CaregiverWithOverlapDto> lastresult = managerContactQueryDsl.findCaregiversByWeeklyMatchingTimeUpdate(serviceSlots);

        Assertions.assertNotNull(lastresult);

        for (CaregiverWithOverlapDto dto : lastresult) {
            System.out.println("Caregiver : " + dto.getCaregiver().getCaregiverTime().get(0).getStartTime() + "~" + dto.getCaregiver().getCaregiverTime().get(0).getEndTime() + " tot : " + dto.getTotalOverlap());
        }
    }
}
