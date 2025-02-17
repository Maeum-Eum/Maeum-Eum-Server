package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.manager.response.ToCaregiverDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.elder.ServiceSlotRepository;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CaregiverMainService {
    private final ManagerContactQueryDsl managerContactQueryDsl;
    private final CaregiverRepository caregiverRepository;
    private final ServiceSlotRepository serviceSlotRepository;

    // n km내 매칭 요청 리스트
    public PageResponse<ToCaregiverDTO> getPages(Double range, Pageable pageable) {

        // 요양보호사 엔티티 확인 및 검증
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 좌표를 문자열 형식으로 변환
        WKTWriter writer = new WKTWriter();
        String wkt = writer.write(caregiver.getLocation());

        // 엔티티 페이지 조회
        Page<ManagerContact> page =
        managerContactQueryDsl.findContactsByFieldAndCenterWithinDistance(wkt, range, pageable, caregiver);

        // 엔티티를 DTO로 변환
        List<ManagerContact> managerContacts = page.getContent();
        List<ToCaregiverDTO> contents = managerContacts.stream()
                .map(this::toDTO)
                .toList();

        return PageResponse.<ToCaregiverDTO>builder()
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(contents)
                .build();
    }

    private ToCaregiverDTO toDTO(ManagerContact contact) { // list에 넣을 dto로 변환
        Elder elder = contact.getElder();
        String title = null;

        // 제목 만들기
        if (serviceSlotRepository.existsByElderAndServiceSlotDayIn(elder, List.of(0,1,2,3,4))) {
            title = "[평일";

            if (serviceSlotRepository.existsByElderAndServiceSlotDayIn(elder, List.of(5,6))) {
                title = title + "/주말] - ";
            }
            else title = title + "] - ";
        }
        else title = "[주말] - ";

        title = title + elder.getElderRank() + "등급 ";
        title = title + ( elder.getGender().equals("male") ? "남자" : "여자" ) + " 어르신";

        return ToCaregiverDTO.builder().contactId(contact.getContactId())
                .center(contact.getManager().getCenter().getCenterName())
                .createdAt(contact.getCreatedAt())
                .wage(contact.getWage())
                .negotiable(contact.isNegotiable())
                .title(title)
                .build();
    }
}
