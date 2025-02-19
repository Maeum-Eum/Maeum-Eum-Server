package com.five.Maeum_Eum.service.user.manager;

import com.five.Maeum_Eum.dto.center.request.ChangeCenterReq;
import com.five.Maeum_Eum.dto.center.request.ModifyCenterReq;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.ApplyCaregiverDto;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.BookmarkCaregiverDto;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.ContactCaregiverDto;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.RecommendedCaregiverDto;
import com.five.Maeum_Eum.dto.user.manager.request.BookmarkReqDto;
import com.five.Maeum_Eum.dto.user.manager.request.ContactReqDto;
import com.five.Maeum_Eum.dto.user.manager.response.BookmarkResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ContactResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.caregiver.WorkDistance;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.repository.caregiver.ApplyRepository;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import com.five.Maeum_Eum.repository.elder.ElderRepository;
import com.five.Maeum_Eum.repository.manager.ManagerBookmarkRepository;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import com.five.Maeum_Eum.repository.manager.ManagerContactRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private final CaregiverService caregiverService;
    private final ApplyRepository applyRepository;
    private final ManagerContactQueryDsl managerContactQueryDsl;

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

        // 지원 목록 - 매니저가 소속된 센터에 소속된 어르신 목록
        List<Elder> elders = elderRepository.findByManagerId(manager.getManagerId());

        int applys = elders.stream()
                .mapToInt(elder -> applyRepository.countByElderId(elder.getElderId()))
                .sum();

        return ManagerBasicDto.from(manager , manager.getCenter() , sentContacts , bookmarks , applys);
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
        // 지원 목록 - 매니저가 소속된 센터에 소속된 어르신 목록
        List<Elder> elders = elderRepository.findByManagerId(manager.getManagerId());

        int applys = elders.stream()
                .mapToInt(elder -> applyRepository.countByElderId(elder.getElderId()))
                .sum();

        ManagerBasicDto managerBasicDto = ManagerBasicDto.from(manager , center , sentContacts , bookmarks, applys);

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

    /* 요양보호사에게 연락한 목록 중 아직 대기 상태인 거 */
    public List<ContactCaregiverDto> getContactList(String token, String name, ApprovalStatus approvalStatus) {

        Manager manager = findManager(token);

        Elder elder = elderRepository.findByElderName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.ELDER_NOT_FOUND));

        List<ManagerContact> managerContacts = managerContactRepository.findByApprovalStatusAndManagerIdAndElderId(manager.getManagerId(),elder.getElderId() ,ApprovalStatus.PENDING);

            List<ContactCaregiverDto> contactCaregiverDtos = managerContacts.stream()
                    .map(managerContact -> {
                        // 제목 생성
                        String title = caregiverService.makeTitle(managerContact.getCaregiver().getResume());
                        // 요양보호사 가능업무 랜덤 3개
                        List<String> combinedList = new ArrayList<>();
                        Resume resume = managerContact.getCaregiver().getResume();

                        if (resume != null) {
                            combinedList.addAll(resume.getToileting() != null ? resume.getToileting() : Collections.emptyList());
                            combinedList.addAll(resume.getMeal() != null ? resume.getMeal() : Collections.emptyList());
                            combinedList.addAll(resume.getDaily() != null ? resume.getDaily() : Collections.emptyList());
                            combinedList.addAll(resume.getMobility() != null ? resume.getMobility() : Collections.emptyList());
                        }

                        Collections.shuffle(combinedList);


                        List<String> randomThree = combinedList.size() > 3 ? combinedList.subList(0, 3) : new ArrayList<>(combinedList);


                        return ContactCaregiverDto.from(managerContact, title , randomThree);
                    })
                    .collect(Collectors.toList());

            return contactCaregiverDtos;

    }

    /* 요양보호사에게 보낸 연락 취소 */
    public String deleteContact(String token, Long contactId) {
        Manager manager = findManager(token);

        ManagerContact managerContact = managerContactRepository.findById(contactId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        if(!manager.getManagerId().equals(managerContact.getManager().getManagerId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        managerContactRepository.delete(managerContact);

        return "연락이 취소되었습니다.";
    }

    /* 북마크 리스트 가져오기 */
    public List<BookmarkCaregiverDto> getBookmarkList(String token, String name) {
        Manager manager = findManager(token);

        Elder elder = elderRepository.findByElderName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.ELDER_NOT_FOUND));

        List<ManagerBookmark> managerBookmarks = managerBookmarkRepository.findByManagerIdAndElderId(manager.getManagerId() , elder.getElderId());

        List<BookmarkCaregiverDto> bookmarkCaregiverDtoList = managerBookmarks.stream()
                .map(managerBookmark -> {
                    List<String> combinedList = new ArrayList<>();
                    combinedList.addAll(managerBookmark.getCaregiver().getResume().getToileting() != null
                            ? managerBookmark.getCaregiver().getResume().getToileting()
                            : Collections.emptyList());

                    combinedList.addAll(managerBookmark.getCaregiver().getResume().getMeal() != null
                            ? managerBookmark.getCaregiver().getResume().getMeal()
                            : Collections.emptyList());

                    combinedList.addAll(managerBookmark.getCaregiver().getResume().getDaily() != null
                            ? managerBookmark.getCaregiver().getResume().getDaily()
                            : Collections.emptyList());

                    combinedList.addAll(managerBookmark.getCaregiver().getResume().getMobility() != null
                            ? managerBookmark.getCaregiver().getResume().getMobility()
                            : Collections.emptyList());

                    Collections.shuffle(combinedList); // 랜덤하게 섞기

                    // 3개 뽑기
                    List<String> randomThree = combinedList.size() > 3 ? combinedList.subList(0, 3) : new ArrayList<>(combinedList);

                    return BookmarkCaregiverDto.from(managerBookmark, randomThree);
                })
                .collect(Collectors.toList());

        return bookmarkCaregiverDtoList;

    }

    /* 지원한 요양보호사 목록 조회 */
    public List<ApplyCaregiverDto> getApplyList(String token, String name, ApprovalStatus approvalStatus) {

        Manager manager = findManager(token);

        Elder elder = elderRepository.findByElderName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.ELDER_NOT_FOUND));

        List<Apply> applyList = applyRepository.findAllByElderAndApprovalStatus(elder.getElderId(), approvalStatus);

        // 지원 목록 중에 해당 노인 요구사항보다 같거나 높은 거 다 노출 , 제목 생성
        List<ApplyCaregiverDto> applyCaregiverDtoList = applyList.stream()
                .map(apply -> {
                    // 제목 생성
                    Caregiver caregiver = applyRepository.findCaregiverByApplyId(apply.getApplyId());
                    String title = makeTitle(caregiver);
                    // 요양보호사 가능업무 다
                    List<String> satisyTasks= new ArrayList<>();
                    Resume resume = caregiver.getResume();

                    if (resume.getMeal() != null) satisyTasks.add("식사");
                    if (resume.getToileting() != null) satisyTasks.add("배변");
                    if (resume.getMobility() != null) satisyTasks.add("이동");
                    if (resume.getDaily() != null) satisyTasks.add("일상");

                    return ApplyCaregiverDto.from(apply , manager , caregiver, title , satisyTasks);
                })
                .collect(Collectors.toList());

        return applyCaregiverDtoList;
    }


    /* 지원 목록용 제목*/
    // 제목 예시 : [월/수/금] 오전 - 식사 배변 이동 일상
    private String makeTitle(Caregiver caregiver){
        String title;
        List<String> workDay = new ArrayList<>();

        List<Integer> workDayDto = caregiver.getResume().getWorkDay();
        for (int idx : workDayDto) {
            switch (idx) {
                case 0: workDay.add("월"); break;
                case 1: workDay.add("화"); break;
                case 2: workDay.add("수"); break;
                case 3: workDay.add("목"); break;
                case 4: workDay.add("금"); break;
                case 5: workDay.add("토"); break;
                case 6: workDay.add("일"); break;
                default: workDay.add("미정");
            }
        }

        String middleWorkDay = String.join("/", workDay);

        List<Integer> workTimeSlots = caregiver.getResume().getWorkTimeSlot();
        List<String> translatedTimeSlots = new ArrayList<>();

        for (Integer slot : workTimeSlots) {
            switch (slot) {
                case 0:
                    translatedTimeSlots.add("오전");
                    break;
                case 1:
                    translatedTimeSlots.add("오후");
                    break;
                case 2:
                    translatedTimeSlots.add("저녁");
                    break;
                default:
                    translatedTimeSlots.add("미정"); // 예외 처리
            }
        }

        String timeSlotString = String.join(", ", translatedTimeSlots);

        List<String> workAttributes = new ArrayList<>();
        Resume resume = caregiver.getResume();

        if (resume.getMeal() != null) workAttributes.add("식사");
        if (resume.getToileting() != null) workAttributes.add("배변");
        if (resume.getMobility() != null) workAttributes.add("이동");
        if (resume.getDaily() != null) workAttributes.add("일상");

        String workAttributesString = String.join(" ", workAttributes);

        title = String.format("[%s] %s - %s", middleWorkDay, timeSlotString, workAttributesString);

        return title;
    }

    /* 추천 요양보호사 목록*/
    public List<RecommendedCaregiverDto> getRecommendedList(String token, String name, String workPlace, String sort) {

        //Manager manager = findManager(token);

        Manager manager = Manager.builder()
                .name("manager")
                .phoneNumber("010-1234-1234")
                .build();

        Elder elder = elderRepository.findByElderName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.ELDER_NOT_FOUND));

        double distance = getDistanceFromWorkPlace(workPlace);

        // 일단 임의로 30개만 조회
        List<Caregiver> caregiverList = managerContactQueryDsl.findCaregiverByFullMatchingSystem(elder , 30 , distance);

        if(caregiverList.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "매칭으로 조회된 유저가 아예 없습니다.");
        }

        // 정렬 및 변환
        return caregiverList.stream()
                .sorted((c1, c2) -> {
                    if (sort.equals("accuracy")) { // 가능 업무가 많은 순 정렬
                        return Integer.compare(getTotalSkills(c2.getResume()), getTotalSkills(c1.getResume()));
                    } else if (sort.equals("time_match")) { // 근무 가능 시간이 많은 순 정렬
                        return Integer.compare(c2.getResume().getWorkTimeSlot().size(), c1.getResume().getWorkTimeSlot().size());
                    } else { // 높은 급여순 정렬
                        return Integer.compare(c2.getResume().getWage(), c1.getResume().getWage());
                    }
                })
                .map(caregiver -> {
                    String title = makeTitle(caregiver);
                    List<String> possibleTasks = extractPossibleTasks(caregiver.getResume());
                    Boolean isMarked = checkIfMarked(manager.getManagerId(), caregiver.getCaregiverId());

                    return RecommendedCaregiverDto.from(caregiver, title, possibleTasks, isMarked);
                }) // Caregiver -> RecommendedCaregiverDto 변환
                .collect(Collectors.toList());
    }

    /* 해당 요양보호사가 북마크가 된 요양보호사인지*/
    private Boolean checkIfMarked(Long managerId, Long caregiverId) {
        if(managerBookmarkRepository.findByManagerIdAndCaregiverId(managerId , caregiverId)){
            return true;
        }
        else return false;
    }

    /* 랜덤으로 최대 7개 뽑기*/
    private List<String> extractPossibleTasks(Resume resume) {
        List<String> combinedList = new ArrayList<>();

        if (resume.getToileting() != null) combinedList.addAll(resume.getToileting());
        if (resume.getMeal() != null) combinedList.addAll(resume.getMeal());
        if (resume.getDaily() != null) combinedList.addAll(resume.getDaily());
        if (resume.getMobility() != null) combinedList.addAll(resume.getMobility());

        // 만약 리스트가 비어 있다면 빈 리스트 반환
        if (combinedList.isEmpty()) return Collections.emptyList();

        // 리스트를 랜덤하게 섞기
        Collections.shuffle(combinedList);

        // 최대 7개까지만 추출 (7개보다 적으면 가능한 만큼만 반환)
        return combinedList.subList(0, Math.min(7, combinedList.size()));
    }

    /*가능한 업무 개수*/
    private int getTotalSkills(Resume resume) {
        return (resume.getMeal() != null ? resume.getMeal().size() : 0) +
                (resume.getToileting() != null ? resume.getToileting().size() : 0) +
                (resume.getMobility() != null ? resume.getMobility().size() : 0) +
                (resume.getDaily() != null ? resume.getDaily().size() : 0);
    }

    /* 거리 변환 */
    private double getDistanceFromWorkPlace(String workPlace) {
        return WorkDistance.fromLabel(workPlace).getDistance();
    }
}
