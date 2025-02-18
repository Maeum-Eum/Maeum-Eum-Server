package com.five.Maeum_Eum;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ElderFamily;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SpringBootTest
@Transactional
public class CaregiverMatchingTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private ManagerContactQueryDsl managerContactQueryDsl;

    @Test
    @DisplayName(value = "최종매칭시스템점검")
    public void testFinal(){

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        // 서울의 위도 및 경도 범위
        double minLatitude = 37.4133;
        double maxLatitude = 37.7013;
        double minLongitude = 126.7341;
        double maxLongitude = 127.2693;

        for (int i = 1; i <= 1000; i++) {

            Caregiver caregiver = Caregiver.builder()
                    .name("[요양사]" + i)
                    .address("서울시" + i)
                    .hasCaregiverCertificate(true)
                    .isResumeRegistered(true)
                    .isJobOpen(false)
                    .jobState(Caregiver.JobState.MATCHED)
                    .phoneNumber("010-1234-5678")
                    .build();

            // 선호 성별
            int randomInt = random.nextInt(3);
            Resume.PreferredGender gender = switch (randomInt){
                case 0 -> Resume.PreferredGender.MALE;
                case 1 -> Resume.PreferredGender.FEMALE;
                case 2 -> Resume.PreferredGender.EVERY;
                default -> throw new IllegalStateException("Unexpected value: " + randomInt);
            };

            // 인지 지원 등급
            List<Integer> ranks = new ArrayList<>();

            int numRanks = random.nextInt(5) + 1; // 1~5개 등급
            for (int j = 0; j < numRanks; j++) { // 최대 5개 등급 넣기
                int rank = random.nextInt(7); // 0~6 범위의 랜덤 등급
                if (!ranks.contains(rank)) {
                    ranks.add(rank);
                }
            }

            // 무작위 위도 및 경도 생성
            double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

            // location
            caregiver.setLocation(geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude)));

            System.out.println("위치" + caregiver.getLocation());

            // 이력서
            Resume resume = Resume.builder()
                    .preferredGender(gender)
                    .caregiver(caregiver)
                    .elderRank(ranks)
                    .mealLevel(random.nextInt(4)+1)
                    .toiletingLevel(random.nextInt(4)+1)
                    .mobilityLevel(random.nextInt(4)+1)
                    .dailyLevel(random.nextInt(6)+1)
                    .elderRankLevel(Collections.max(ranks))
                    .wage((random.nextInt(7000)+13000))
                    .build();

            caregiver.setResume(resume);
            em.persist(caregiver);
            em.persist(resume);
            em.flush();
        }

        // 어르신 샘플 데이터 생성

        // 무작위 위도 및 경도 생성
        double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
        double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

        // location
        Elder elder = Elder.builder()
                .elderName("홍길동")
                .gender("MALE")
                .elder_family(ElderFamily.IN_HOME)
                .elder_pet(true)
                .elderAddress("서울시 강남구 역삼동")
                .elderBirth(LocalDate.of(1955,7,7))
                .meal(Arrays.asList("스스로식사가능"))
                .mealLevel(1)
                .location(geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude)))
                .toileting(Arrays.asList("기저귀케어필요"))
                .toiletingLevel(3)

                .mobility(Arrays.asList("거동불가"))
                .mobilityLevel(4)

                .daily(Arrays.asList("목욕보조"))
                .dailyLevel(2)
                .wage(16000)
                .elderRank(3)
                .build();


        em.persist(elder);
        em.flush();

        // 서비스 슬롯
        List<ServiceSlot> serviceSlots = new ArrayList<>();
        ServiceSlot serviceSlot = ServiceSlot.builder()
                .elder(elder)
                .serviceSlotDay(DayOfWeek.FRI)
                .serviceSlotStart(LocalTime.of(14,0))
                .serviceSlotEnd(LocalTime.of(15,40))
                .build();
        serviceSlots.add(serviceSlot);
        elder.setServiceSlots(serviceSlots);
        em.persist(serviceSlot);
        em.flush();

        // 쿼리 조회 - 어르신에 맞는 10명 가져옴, 반경 5KM 이내
        List<Caregiver> caregivers = managerContactQueryDsl.findCaregiverByFullMatchingSystem(elder, 10, 5);

        Assertions.assertNotNull(caregivers);
        Assertions.assertFalse(caregivers.isEmpty());

        // 어르신 정보
        System.out.println("[어르신] " + elder.getGender()
                + "[장기요양등급]" + elder.getElderRank()
                + "[시급]" + elder.getWage()
                + "[식사]" + elder.getMealLevel()
                + "[이동]"+ elder.getMobilityLevel()
                + "[배변]"+ elder.getToiletingLevel()
                + "[일상]" + elder.getDailyLevel()
                + "[위치] " + elder.getLocation());

        for (Caregiver c : caregivers) {
            System.out.println("[LOG] " + c.getName());
            // gender
            System.out.print(" 선호 성별 : " + c.getResume().getPreferredGender()
                + "[서비스가능등급]" + c.getResume().getElderRankLevel()
                    + "[시급]" + c.getResume().getWage()
                    + "[식사]" + c.getResume().getMealLevel()
                    + "[이동]"+ c.getResume().getMobilityLevel()
                    + "[배변]"+ c.getResume().getToiletingLevel()
                    + "[일상]"+ c.getResume().getDailyLevel()
                    + "[위치] " + c.getLocation());
        }
    }
}
