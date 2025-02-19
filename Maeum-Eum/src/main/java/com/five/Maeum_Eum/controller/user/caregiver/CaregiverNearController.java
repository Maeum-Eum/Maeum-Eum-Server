package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.ContactAcceptDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.DetailContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.dto.user.elder.response.ElderListDTO;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caregiver/near")
public class CaregiverNearController {

    private final CaregiverMainService caregiverMainService;

    @PostMapping("/bookmark")
    public ResponseEntity<Object> bookmarkApply(@RequestParam Long elderId) {
        caregiverMainService.applyBookmark(elderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getDetail(@RequestParam Long elderId) {
        DetailContactDTO body = caregiverMainService.applyDetail(elderId);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<Object> apply(@RequestParam Long elderId, @RequestBody ContactAcceptDTO acceptDTO) {
        ContactAcceptDTO body = caregiverMainService.apply(elderId, acceptDTO);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getList(@RequestParam Double range) {
        ElderListDTO body = caregiverMainService.nearElder(range);
        return ResponseEntity.ok().body(body);
    }
}
