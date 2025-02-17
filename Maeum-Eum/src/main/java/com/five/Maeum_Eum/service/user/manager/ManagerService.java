package com.five.Maeum_Eum.service.user.manager;

import com.five.Maeum_Eum.dto.center.request.ChangeCenterReq;
import com.five.Maeum_Eum.dto.center.request.ModifyCenterReq;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.dto.user.manager.request.BookmarkReqDto;
import com.five.Maeum_Eum.dto.user.manager.request.ContactReqDto;
import com.five.Maeum_Eum.dto.user.manager.response.BookmarkResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ContactResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import com.five.Maeum_Eum.repository.elder.ElderRepository;
import com.five.Maeum_Eum.repository.manager.ManagerBookmarkRepository;
import com.five.Maeum_Eum.repository.manager.ManagerContactRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final JWTUtil jwtUtil;
    private final ManagerRepository managerRepository;
    private final ManagerContactRepository managerContactRepository;
    private final ManagerBookmarkRepository managerBookmarkRepository;
    private final CenterRepository centerRepository;
    private final CaregiverRepository caregiverRepository;
    private final ElderRepository elderRepository;

    // token으로  사용자 role 알아내기
    private String findRole(String token){
        return jwtUtil.getRole(token);
    }

    // token으로 사용자 loginId 알아내기
    private String findLoginId(String token){
        return jwtUtil.getId(token);
    }


    private Manager findManager(String token){
        if(!findRole(token).equals("ROLE_MANAGER")){ // 사용자가 관리자 역할이 아닐 때
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        Manager manager = managerRepository.findByLoginId(findLoginId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return manager;
    }

    // 관리자의 기본 정보 조회
    public ManagerBasicDto getManagerBasicInfo(String token) {

        Manager manager = findManager(token);


        // 요양보호사에게 보낸 연락 개수
        int sentContacts = managerContactRepository.countManagerContactByManagerId(manager.getManagerId());

        // 요양보호사 북마크 개수
        int bookmarks = managerBookmarkRepository.countManagerBookmarkByManagerId(manager.getManagerId());

        return ManagerBasicDto.from(manager , manager.getCenter() , sentContacts , bookmarks);
    }

    /* 센터 정보 수정 */
    public CenterDTO modifyCenterInfo(String token, Long centerId , ModifyCenterReq modifyCenterReq) {


        Manager manager = findManager(token);

        if(!manager.getCenter().getCenterId().equals(centerId)){ // 사용자가 소속된 center id와 일치하지 않을 때
            throw  new CustomException(ErrorCode.INVALID_ROLE);
        }

        Center center = centerRepository.findByCenterId(manager.getCenter().getCenterId())
                .orElseThrow(() -> new CustomException(ErrorCode.CENTER_NOT_FOUND));


        center.updateOneLineIntro(modifyCenterReq.getOneLineIntro());
        centerRepository.save(center);

        Center modifyCenter = centerRepository.findByCenterId(center.getCenterId())
                .orElseThrow(() -> new CustomException(ErrorCode.CENTER_NOT_FOUND));

        CenterDTO centerDTO = CenterDTO.builder()
                .centerId(modifyCenter.getCenterId())
                .centerName(modifyCenter.getCenterName())
                .centerCode(modifyCenter.getCenterCode())
                .zipCode(modifyCenter.getZipCode())
                .designatedTime(modifyCenter.getDesignatedTime())
                .installationTime(modifyCenter.getInstallationTime())
                .detailAddress(modifyCenter.getDetailAddress())
                .address(modifyCenter.getAddress())
                .finalGrade(modifyCenter.getFinalGrade())
                .oneLineIntro(modifyCenter.getOneLineIntro())
                .build();

        return  centerDTO;
    }

    /* 관라지가 소속된 센터 변경 */
    public ManagerBasicDto changeCenter(String token, ChangeCenterReq centerReq) {


        Manager manager = findManager(token);

        Center center = centerRepository.findByCenterId(centerReq.getCenterId())
                .orElseThrow(() -> new CustomException(ErrorCode.CENTER_NOT_FOUND));

        manager.changeCenter(center);
        managerRepository.save(manager);

        if(centerReq.getHasCar()!= null && centerReq.getHasCar().isPresent()){
            center.registerManager(centerReq.getHasCar().get());
            centerRepository.save(center);
        }

        if(centerReq.getOneLineIntro() != null && centerReq.getOneLineIntro().isPresent()){
            center.updateOneLineIntro(centerReq.getOneLineIntro().get());
            centerRepository.save(center);
        }

        // 요양보호사에게 보낸 연락 개수
        int sentContacts = managerContactRepository.countManagerContactByManagerId(manager.getManagerId());

        // 요양보호사 북마크 개수
        int bookmarks = managerBookmarkRepository.countManagerBookmarkByManagerId(manager.getManagerId());

        ManagerBasicDto managerBasicDto = ManagerBasicDto.from(manager , center , sentContacts , bookmarks);

        return managerBasicDto;
    }

    /* 관리자가 요양보호사에게 연락하기 */
    public ContactResDto contactToCaregiver(String token, Long caregiverId, ContactReqDto contactReqDto) {
        Manager manager = findManager(token);

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Elder elder = elderRepository.findById(contactReqDto.getElderId())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        ManagerContact managerContact = ContactReqDto.toEntity(manager , caregiver , elder , contactReqDto);

        managerContactRepository.save(managerContact);

        ContactResDto contactResDto = ContactResDto.from(managerContact);

        return contactResDto;

    }

    /* 북마크 하기 */
    public BookmarkResDto bookmarkCaregiver(String token, BookmarkReqDto dto) {
        Manager manager = findManager(token);

        Caregiver caregiver = caregiverRepository.findById(dto.getCaregiverId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        ManagerBookmark managerBookmark = BookmarkReqDto.toEntity(manager , elder , caregiver);

        managerBookmarkRepository.save(managerBookmark);

        BookmarkResDto resDto = BookmarkResDto.from(managerBookmark);

        return resDto;
    }

    /* 북마크 삭제*/
    public String deleteBookmark(String token, Long bookmarkId) {
        Manager manager = findManager(token);
        ManagerBookmark managerBookmark = managerBookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        if(!manager.getManagerId().equals(managerBookmark.getManager().getManagerId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        managerBookmarkRepository.delete(managerBookmark);

        return "관리자의 북마크가 삭제되었습니다.";
    }
}
