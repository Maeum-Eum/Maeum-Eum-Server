package com.five.Maeum_Eum.service.user.elder;

import com.five.Maeum_Eum.controller.user.service.DailyElderType;
import com.five.Maeum_Eum.controller.user.service.MealElderType;
import com.five.Maeum_Eum.controller.user.service.MobilityElderType;
import com.five.Maeum_Eum.controller.user.service.ToiletingElderType;
import com.five.Maeum_Eum.controller.work.MobilityType;
import com.five.Maeum_Eum.dto.user.elder.request.ElderCreateDTO;
import com.five.Maeum_Eum.dto.user.elder.request.TimeDTO;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ElderFamily;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.elder.ElderRepository;
import com.five.Maeum_Eum.repository.elder.ServiceSlotRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final ManagerRepository managerRepository;
    private final KakaoAddressService kakaoAddressService;
    private final ServiceSlotRepository serviceSlotRepository;

    public boolean createElder (ElderCreateDTO dto) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if (!role.equals("ROLE_MANAGER")) { throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Manager manager = managerRepository.findByLoginId(managerId).orElse(null);
        if (manager == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);}

        // 입력 검증
        for (ConstraintViolation<ElderCreateDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(dto)) {
            System.out.println(violation.getPropertyPath().toString());
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // family 필드 변환
        ElderFamily family = ElderFamily.fromValue(dto.getFamily());
        if (family == null) throw new CustomException(ErrorCode.INVALID_INPUT);

        // 주소 -> 좌표 변환
        Point location = kakaoAddressService.getCoordinates(dto.getAddress());

        // 성명 중복 검사
        long cnt = elderRepository.countByElderName(dto.getName());
        if ( cnt > 0)
            dto.setName(dto.getName() + " (" + cnt + ")");

        Elder elder = Elder.builder()
                .elderName(dto.getName())
                .gender(dto.getGender())
                .elderBirth(dto.getBirth())
                .elderAddress(dto.getAddress())
                .elderRank(dto.getRank())
                .negotiable(dto.getNegotiable())
                .wage(dto.getWage())
                .manager(manager)
                .meal(dto.getMeal())
                .mobility(dto.getMobility())
                .toileting(dto.getToileting())
                .daily(dto.getDaily())
                .elder_family(family)
                .elder_pet(dto.getPet().equals("있어요"))
                .location(location)

                // 서비스 요구 단계
                .mealLevel(
                       dto.getMeal().stream()
                                .map(MealElderType::fromLabel)
                                .mapToInt(MealElderType::getLevel)
                                .max()
                                .orElse(0)
                )
                .dailyLevel(
                        dto.getDaily().stream()
                                .map(DailyElderType::fromLabel)
                                .mapToInt(DailyElderType::getLevel)
                                .max()
                                .orElse(0)
                )
                .mobilityLevel(
                        dto.getMobility().stream()
                                .map(MobilityElderType::fromLabel)
                                .mapToInt(MobilityElderType::getLevel)
                                .max()
                                .orElse(0)
                )
                .toiletingLevel(
                        dto.getToileting().stream()
                                .map(ToiletingElderType::fromLabel)
                                .mapToInt(ToiletingElderType::getLevel)
                                .max()
                                .orElse(0)
                )
                .build();


        elderRepository.save(elder);

        ServiceSlot[] serviceSlot = new ServiceSlot[7];
        if (dto.getMon() != null) {
            serviceSlot[0] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.MON)
                    .serviceSlotStart(dto.getMon().getStart())
                    .serviceSlotEnd(dto.getMon().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[0]);
        }
        if (dto.getTue() != null) {
            serviceSlot[1] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.TUE)
                    .serviceSlotStart(dto.getTue().getStart())
                    .serviceSlotEnd(dto.getTue().getEnd())
                    .build();
            serviceSlotRepository.save(serviceSlot[1]);
        }
        if (dto.getWed() != null) {
            serviceSlot[2] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.WED)
                    .serviceSlotStart(dto.getWed().getStart())
                    .serviceSlotEnd(dto.getWed().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[2]);
        }
        if (dto.getThu() != null) {
            serviceSlot[3] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.THU)
                    .serviceSlotStart(dto.getThu().getStart())
                    .serviceSlotEnd(dto.getThu().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[3]);
        }
        if (dto.getFri() != null) {
            serviceSlot[4] = ServiceSlot.builder()
                    .serviceSlotDay(DayOfWeek.FRI)
                    .serviceSlotStart(dto.getFri().getStart())
                    .serviceSlotEnd(dto.getFri().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[4]);
        }
        if (dto.getSat() != null) {
            serviceSlot[5] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.SAT)
                    .serviceSlotStart(dto.getSat().getStart())
                    .serviceSlotEnd(dto.getSat().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[5]);
        }
        if (dto.getSun() != null) {
            serviceSlot[6] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.SUN)
                    .serviceSlotStart(dto.getSun().getStart())
                    .serviceSlotEnd(dto.getSun().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[6]);
        }
        return true;
    }

    public ElderCreateDTO getElder(Long elderId) {
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Manager manager = managerRepository.findByLoginId(managerId).orElse(null);
        if (manager == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);}

        Elder elder = elderRepository.findById(elderId).orElse(null);
        if (elder == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND, "등록되지 않은 어르신입니다");}

        List<ServiceSlot> serviceSlots = elder.getServiceSlots();
        System.out.println(serviceSlots);
        TimeDTO[] timeDTO = new TimeDTO[7];

        for (ServiceSlot serviceSlot : serviceSlots) {
            switch (serviceSlot.getServiceSlotDay()) {
                case MON: timeDTO[0] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case TUE: timeDTO[1] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case WED: timeDTO[2] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case THU: timeDTO[3] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case FRI: timeDTO[4] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case SAT: timeDTO[5] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
                case SUN: timeDTO[6] = TimeDTO.builder().start(serviceSlot.getServiceSlotStart()).end(serviceSlot.getServiceSlotEnd()).build(); break;
            }
        }

        return ElderCreateDTO.builder()
                .elderId(elderId)
                .name(elder.getElderName())
                .gender(elder.getGender())
                .birth(elder.getElderBirth())
                .rank(elder.getElderRank())
                .address(elder.getElderAddress())
                .mon(timeDTO[0])
                .thu(timeDTO[1])
                .wed(timeDTO[2])
                .thu(timeDTO[3])
                .fri(timeDTO[4])
                .sat(timeDTO[5])
                .sun(timeDTO[6])
                .pet(elder.getElder_pet() ? "있어요" : "없어요")
                .family(elder.getElder_family().getValue())
                .wage(elder.getWage())
                .build();
    }

    public void editElder(ElderCreateDTO dto, Long elderId) {
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Manager manager = managerRepository.findByLoginId(managerId).orElse(null);
        if (manager == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);}


        // 입력 검증
        for (ConstraintViolation<ElderCreateDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(dto)) {
            System.out.println(violation.getPropertyPath().toString());
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Elder elder = elderRepository.findById(elderId).orElse(null);
        if (elder == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND, "등록되지 않은 어르신입니다");}

        ElderFamily family = ElderFamily.fromValue(dto.getFamily());
        if (family == null) throw new CustomException(ErrorCode.INVALID_INPUT);

        // 주소 -> 좌표 변환
        Point location = kakaoAddressService.getCoordinates(dto.getAddress());

        elder.update(
                dto.getGender(),
                dto.getBirth(),
                dto.getAddress(),
                dto.getRank(),
                dto.getNegotiable(),
                dto.getWage(),
                manager,
                dto.getMeal(),
                dto.getToileting(),
                dto.getMobility(),
                dto.getDaily(),
                family,
                dto.getPet().equals("있어요"),
                location
        );
        elderRepository.save(elder);

        serviceSlotRepository.deleteByElder(elder);
        ServiceSlot[] serviceSlot = new ServiceSlot[7];
        if (dto.getMon() != null) {
            serviceSlot[0] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.MON)
                    .serviceSlotStart(dto.getMon().getStart())
                    .serviceSlotEnd(dto.getMon().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[0]);
        }
        if (dto.getTue() != null) {
            serviceSlot[1] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.TUE)
                    .serviceSlotStart(dto.getTue().getStart())
                    .serviceSlotEnd(dto.getTue().getEnd())
                    .build();
            serviceSlotRepository.save(serviceSlot[1]);
        }
        if (dto.getWed() != null) {
            serviceSlot[2] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.WED)
                    .serviceSlotStart(dto.getWed().getStart())
                    .serviceSlotEnd(dto.getWed().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[2]);
        }
        if (dto.getThu() != null) {
            serviceSlot[3] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.THU)
                    .serviceSlotStart(dto.getThu().getStart())
                    .serviceSlotEnd(dto.getThu().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[3]);
        }
        if (dto.getFri() != null) {
            serviceSlot[4] = ServiceSlot.builder()
                    .serviceSlotDay(DayOfWeek.FRI)
                    .serviceSlotStart(dto.getFri().getStart())
                    .serviceSlotEnd(dto.getFri().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[4]);
        }
        if (dto.getSat() != null) {
            serviceSlot[5] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.SAT)
                    .serviceSlotStart(dto.getSat().getStart())
                    .serviceSlotEnd(dto.getSat().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[5]);
        }
        if (dto.getSun() != null) {
            serviceSlot[6] = ServiceSlot.builder()
                    .elder(elder)
                    .serviceSlotDay(DayOfWeek.SUN)
                    .serviceSlotStart(dto.getSun().getStart())
                    .serviceSlotEnd(dto.getSun().getEnd())
                    .build();

            serviceSlotRepository.save(serviceSlot[6]);
        }
    }

}
