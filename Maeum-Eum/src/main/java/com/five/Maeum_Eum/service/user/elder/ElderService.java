package com.five.Maeum_Eum.service.user.elder;

import com.five.Maeum_Eum.dto.user.elder.request.ElderCreateDTO;
import com.five.Maeum_Eum.dto.user.register.request.ManagerRegiDTO;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ElderFamily;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.elder.ElderRepository;
import com.five.Maeum_Eum.repository.elder.ServiceSlotRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!managerRepository.existsByLoginId(managerId)) { throw new CustomException(ErrorCode.USER_NOT_FOUND);}


        // 입력 검증
        for (ConstraintViolation<ElderCreateDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(dto)) {
            System.out.println(violation.getPropertyPath().toString());
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // family 필드 변환
        ElderFamily family;
        if (dto.getFamily().equals("관계없어요")) family = ElderFamily.NO_FAMILY;
        else if (dto.getFamily().equals("집에 없어요")) family = ElderFamily.NOT_IN_HOME;
        else if (dto.getFamily().equals("집에 있어요")) family = ElderFamily.IN_HOME;
        else throw new CustomException(ErrorCode.INVALID_INPUT);


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
                .meal(dto.getMeal())
                .mobility(dto.getMobility())
                .toileting(dto.getToileting())
                .daily(dto.getDaily())
                .elder_family(family)
                .elder_pet(dto.getPet().equals("있어요"))
                .location(location)
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

}
