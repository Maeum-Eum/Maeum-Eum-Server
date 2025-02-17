package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.ContactAcceptDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.DetailContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caregiver/main")
public class CaregiverMainController {

    private final CaregiverMainService caregiverMainService;

    @GetMapping("/list")
    public ResponseEntity<Object> getList(@PageableDefault(size = 5) Pageable pageable,
                                                 @RequestParam Double range) {
        PageResponse<SimpleContactDTO> body = caregiverMainService.getPages(range, pageable);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping
    public ResponseEntity<Object> getDetail(@RequestParam Long contactId) {
        DetailContactDTO body = caregiverMainService.getDetail(contactId);
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/accept")
    public ResponseEntity<Object> accept(@RequestBody ContactAcceptDTO acceptDTO,
                                         @RequestParam Long contactId) {
        ContactAcceptDTO body = caregiverMainService.accept(acceptDTO, contactId);
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/reject")
    public ResponseEntity<Object> reject(@RequestParam Long contactId) {
        caregiverMainService.reject(contactId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bookmark")
    public ResponseEntity<Object> bookmark(@RequestParam Long contactId) {
        caregiverMainService.bookmark(contactId);
        return ResponseEntity.ok().build();
    }
}
