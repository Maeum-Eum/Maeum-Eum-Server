package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.ContactAcceptDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.DetailContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.dto.user.elder.response.ElderInfoDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.SavedElders;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.caregiver.ApplyRepository;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.elder.ElderRepository;
import com.five.Maeum_Eum.repository.elder.SavedEldersRepository;
import com.five.Maeum_Eum.repository.elder.ServiceSlotRepository;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import com.five.Maeum_Eum.repository.manager.ManagerContactRepository;
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
    private final ManagerContactRepository managerContactRepository;
    private final SavedEldersRepository savedEldersRepository;
    private final ElderRepository elderRepository;
    private final ApplyRepository applyRepository;

    // n km내 매칭 요청 리스트
    public PageResponse<SimpleContactDTO> getPages(Double range, Pageable pageable) {

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
        List<SimpleContactDTO> contents = managerContacts.stream()
                .map(this::toDTO)
                .toList();

        return PageResponse.<SimpleContactDTO>builder()
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(contents)
                .build();
    }

    // 매칭 자세히 보기
    public DetailContactDTO getDetail(long id) {

        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        ManagerContact contact = managerContactRepository.findById(id).orElse(null);
        if (contact == null || !contact.getApprovalStatus().equals(ApprovalStatus.PENDING)) { throw new CustomException(ErrorCode.CONTACT_NOT_FOUND); }

        if (!contact.getCaregiver().equals(caregiver)) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        Center center = contact.getManager().getCenter();
        CenterDTO centerDTO = CenterDTO.builder()
                .centerName(center.getCenterName())
                .hasCar(center.isHasCar())
                .finalGrade(center.getFinalGrade())
                .installationTime(center.getInstallationTime())
                .build();

        Elder elder = contact.getElder();
        ElderInfoDTO elderInfoDTO = ElderInfoDTO.builder()
                .rank(elder.getElderRank())
                .gender(elder.getGender())
                .address(elder.getElderAddress())
                .meal(elder.getMeal())
                .toileting(elder.getToileting())
                .mobility(elder.getMobility())
                .daily(elder.getDaily())
                .family(elder.getElder_family().getValue())
                .pet(elder.getElder_pet() ? "있어요" : "없어요")
                .build();

        return DetailContactDTO.builder()
                .contactId(contact.getContactId())
                .center(centerDTO)
                .message(contact.getMessageFromManager())
                .title(getTitle(contact.getElder(), contact.getWorkRequirement()))
                .createdAt(contact.getCreatedAt())
                .wage(contact.getWage())
                .negotiable(contact.isNegotiable())
                .bookmarked(savedEldersRepository.findByElderAndCaregiver(elder, caregiver).isPresent())
                .elder(elderInfoDTO)
                .build();

    }

    // 요청 수락
    public ContactAcceptDTO accept(ContactAcceptDTO acceptDTO, Long id) {

        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        ManagerContact contact = managerContactRepository.findById(id).orElse(null);
        if (contact == null) { throw new CustomException(ErrorCode.CONTACT_NOT_FOUND); }

        if (!contact.getCaregiver().equals(caregiver)) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        contact.approve(acceptDTO.getMessage(), acceptDTO.getPhone());
        managerContactRepository.save(contact);


        return ContactAcceptDTO.builder()
                .phone(contact.getManagerPhoneNumber())
                .build();
    }

    // 요청 거절
    public boolean reject(Long id) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        ManagerContact contact = managerContactRepository.findById(id).orElse(null);
        if (contact == null || !contact.getApprovalStatus().equals(ApprovalStatus.PENDING)) { throw new CustomException(ErrorCode.CONTACT_NOT_FOUND); }

        if (!contact.getCaregiver().equals(caregiver)) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }


        contact.reject();
        managerContactRepository.save(contact);


        return true;
    }

    // 직접 지원하기
    public void apply(Long elderId, ContactAcceptDTO dto) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Elder elder = elderRepository.findById(elderId).orElse(null);
        if (elder == null) { throw new CustomException(ErrorCode.INVALID_INPUT); }

        if (applyRepository.existsByCaregiverAndElder(caregiver, elder)) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        Apply apply = Apply.builder()
                .caregiver(caregiver)
                .elder(elder)
                .approvalStatus(ApprovalStatus.PENDING)
                .caregiverPhoneNumber(dto.getPhone())
                .messageFromCaregiver(dto.getMessage())
                .workRequirement("test")
                .managerPhoneNumber(elder.getManager().getPhoneNumber())
                .build();

        applyRepository.save(apply);
    }

    public DetailContactDTO applyDetail(long id) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Elder elder = elderRepository.findById(id).orElse(null);
        if (elder == null) { throw new CustomException(ErrorCode.INVALID_INPUT); }

        Center center = elder.getManager().getCenter();
        CenterDTO centerDTO = CenterDTO.builder()
                .centerName(center.getCenterName())
                .hasCar(center.isHasCar())
                .finalGrade(center.getFinalGrade())
                .installationTime(center.getInstallationTime())
                .build();

        ElderInfoDTO elderInfoDTO = ElderInfoDTO.builder()
                .rank(elder.getElderRank())
                .gender(elder.getGender())
                .address(elder.getElderAddress())
                .meal(elder.getMeal())
                .toileting(elder.getToileting())
                .mobility(elder.getMobility())
                .daily(elder.getDaily())
                .family(elder.getElder_family().getValue())
                .pet(elder.getElder_pet() ? "있어요" : "없어요")
                .build();

        return DetailContactDTO.builder()
                .elderId(id)
                .center(centerDTO)
                .title(getTitle(elder, "test"))
                .wage(elder.getWage())
                .negotiable(elder.isNegotiable())
                .bookmarked(savedEldersRepository.findByElderAndCaregiver(elder, caregiver).isPresent())
                .elder(elderInfoDTO)
                .build();

    }

    // 요청 북마크
    public void contactBookmark(Long id) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        ManagerContact contact = managerContactRepository.findById(id).orElse(null);
        if (contact == null || !contact.getApprovalStatus().equals(ApprovalStatus.PENDING)) { throw new CustomException(ErrorCode.CONTACT_NOT_FOUND); }

        if (!contact.getCaregiver().equals(caregiver)) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        Elder elder = contact.getElder();
        bookmark(elder, caregiver);

    }

    // 지원 페이지 어르신 북마크
    public void applyBookmark(Long id) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Elder elder = elderRepository.findById(id).orElse(null);
        if (elder == null) { throw new CustomException(ErrorCode.INVALID_INPUT); }

        bookmark(elder, caregiver);
    }

    private void bookmark(Elder elder, Caregiver caregiver) {
        SavedElders savedElders = savedEldersRepository.findByElderAndCaregiver(elder, caregiver).orElse(null);

        if (savedElders != null) {
            savedEldersRepository.delete(savedElders);
        }
        else {
            savedElders = SavedElders.builder()
                    .caregiver(caregiver)
                    .elder(elder).build();
            savedEldersRepository.save(savedElders);
        }
    }


    private String getTitle(Elder elder, String requirement) {
        String title = null;

        // 제목 만들기
        if (serviceSlotRepository.existsByElderAndServiceSlotDayIn(elder, List.of(0,1,2,3,4))) {
            title = "[평일";

            if (serviceSlotRepository.existsByElderAndServiceSlotDayIn(elder, List.of(5,6))) {
                title = title + "/주말] ";
            }
            else title = title + "] ";
        }
        else title = "[주말] ";

        title = title + requirement + " - ";

        title = title + elder.getElderRank() + "등급 ";
        title = title + ( elder.getGender().equals("male") ? "남자" : "여자" ) + " 어르신";
        return title;
    }

    public SimpleContactDTO toDTO(ManagerContact contact) { // list에 넣을 dto로 변환
        Elder elder = contact.getElder();
        Caregiver caregiver = contact.getCaregiver();

        return SimpleContactDTO.builder().contactId(contact.getContactId())
                .center(contact.getManager().getCenter().getCenterName())
                .createdAt(contact.getCreatedAt())
                .wage(contact.getWage())
                .negotiable(contact.isNegotiable())
                .bookmarked(savedEldersRepository.findByElderAndCaregiver(elder, caregiver).isPresent())
                .title(getTitle(elder, contact.getWorkRequirement()))
                .build();
    }

    public SimpleContactDTO toDTO(Apply apply) { // list에 넣을 dto로 변환
        Elder elder = apply.getElder();

        return SimpleContactDTO.builder().applyId(apply.getApplyId())
                .center(elder.getManager().getCenter().getCenterName())
                .createdAt(apply.getCreatedAt())
                .wage(elder.getWage())
                .negotiable(elder.isNegotiable())
                .title(getTitle(elder, apply.getWorkRequirement()))
                .build();
    }
}
