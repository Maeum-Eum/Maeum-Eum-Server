package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.main.response.ContactAcceptDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.DetailContactDTO;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverMainService;
import lombok.RequiredArgsConstructor;
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
        caregiverMainService.apply(elderId, acceptDTO);
        return ResponseEntity.ok().build();
    }
}
