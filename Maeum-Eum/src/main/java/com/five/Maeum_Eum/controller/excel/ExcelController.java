package com.five.Maeum_Eum.controller.excel;

import com.five.Maeum_Eum.service.excel.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/center")
    public ResponseEntity<Object> uploadExcelFile(@RequestParam("file") MultipartFile file) {

        // 1. 파일의 확장자를 통해 엑셀 파일인지 검사하고 아니라면 오류 반환

        // 2. 정상인 경우 엑셀 업로드
        excelService.saveCenterData(file);
        return ResponseEntity.ok("파일 저장 성공");
    }
}