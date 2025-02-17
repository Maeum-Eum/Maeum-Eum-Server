package com.five.Maeum_Eum;

import com.five.Maeum_Eum.dto.manager.CaregiverWithOverlapDto;
import com.five.Maeum_Eum.entity.CaregiverTime;
import com.five.Maeum_Eum.entity.QCaregiverTime;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

@SpringBootTest
@Transactional
public class CaregiverMatchingTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ManagerContactQueryDsl managerContactQueryDsl;

    @Test
    @DisplayName(value = "test")
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
            System.out.println("Matched Caregiver: " + caregiver.getName() + " 겹치는 시간 : " + totalOverlap
            );
        }

        // workDay 가 화요일인 요양보호사를 모두 조회해오는 쿼리

        QCaregiverTime caregiverTime = QCaregiverTime.caregiverTime;
        QCaregiver caregiver = QCaregiver.caregiver;

        JPAQueryFactory query = new JPAQueryFactory(em);

        // 요양보호사와 해당 근무시간 정보를 조인하여, workDay가 화요일인 요양보호사 조회

        List<Caregiver> res = query
                .select(caregiver)
                .from(caregiverTime)
                .join(caregiverTime.caregiver, caregiver)
                .where(caregiverTime.workDay.eq(DayOfWeek.TUE))
                .fetch();

        // System.out.println("요양보호사 모두 가져오기 : " +query.select(caregiver.name).from(caregiverTime).fetch());

        for(Caregiver c : res)
        {
            // System.out.println("근무날이 화요일인 요양 보호사 목록 : "+ c.getName());
        }
        Assertions.assertNotNull(res);
    }
}
