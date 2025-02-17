package com.five.Maeum_Eum.controller.user.manager;

import com.five.Maeum_Eum.dto.center.request.ChangeCenterReq;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.CaregiverDto;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.dto.user.manager.request.BookmarkReqDto;
import com.five.Maeum_Eum.dto.user.manager.request.ContactReqDto;
import com.five.Maeum_Eum.dto.user.manager.response.BookmarkResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ContactResDto;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import com.five.Maeum_Eum.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final CaregiverService caregiverService;

    private String extractToken(String authHeader){
        return authHeader.substring(7).trim();
    }

    /* 관리자 마이페이지 */
    @GetMapping("/mypage")
    public ResponseEntity<ManagerBasicDto> getManagerMypage(@RequestHeader("Authorization") String authHeader){
        String token = extractToken(authHeader);
         ManagerBasicDto managerBasicDto = managerService.getManagerBasicInfo(token);
         return ResponseEntity.status(HttpStatus.OK)
                 .body(managerBasicDto);
    }

    /* 관리자가 소속된 센터 변경 */
    @PatchMapping("/center")
    public ResponseEntity<ManagerBasicDto> changerCenter(@RequestHeader("Authorization") String authHeader , @RequestBody ChangeCenterReq centerReq){
        String token = extractToken(authHeader);
        ManagerBasicDto managerBasicDto = managerService.changeCenter(token , centerReq);
        return ResponseEntity.ok(managerBasicDto);
    }


    /* 관리자가 요양보호사의 이력서 조회 */
    @GetMapping("/{caregiverId}")
    public ResponseEntity<ResumeResponseDTO> getCaregiverResume(@RequestHeader("Authorization") String authHeader , @PathVariable("caregiverId")Long caregiverId){
        String token = extractToken(authHeader);
        ResumeResponseDTO responseDTO = caregiverService.getCaregiverResume(token , caregiverId);
        return  ResponseEntity.ok(responseDTO);
    }


    /* 관리자가 요양보호사에게 연락하기*/
    @PostMapping("/{caregiverId}")
    public ResponseEntity<ContactResDto> contactCaregiver(@RequestHeader("Authorization") String authHeader , @PathVariable("caregiverId")Long caregiverId ,@RequestBody ContactReqDto contactReqDto ){
        String token = extractToken(authHeader);
        ContactResDto contactDto = managerService.contactToCaregiver(token , caregiverId , contactReqDto);
        return ResponseEntity.ok(contactDto);

    }

    /* 요양보호사 북마크하기 */
    @PostMapping("/bookmark")
    public ResponseEntity<BookmarkResDto> bookmarkCaregiver(@RequestHeader("Authorization") String authHeader , @RequestBody BookmarkReqDto dto){
        String token = extractToken(authHeader);
        BookmarkResDto resDto = managerService.bookmarkCaregiver(token , dto);
        return ResponseEntity.ok(resDto);
    }


    /* 요양보호사 북마크 취소 */
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(@RequestHeader("Authorization") String authHeader , @PathVariable("bookmarkId")Long bookmarkId){
        String token = extractToken(authHeader);
        String response = managerService.deleteBookmark(token , bookmarkId);
        return ResponseEntity.ok(response);
    }


    /* 연락 보낸 대기 목록 */
    @GetMapping("contact")
    public ResponseEntity<?> getContact(@RequestHeader("Authorization") String authHeader ,@RequestParam(name ="name") String name) {
        String token = extractToken(authHeader);
        List<CaregiverDto> caregiverDtoList = managerService.getContactList(token , name , ApprovalStatus.PENDING);
        return ResponseEntity.ok(caregiverDtoList);
    }



}
