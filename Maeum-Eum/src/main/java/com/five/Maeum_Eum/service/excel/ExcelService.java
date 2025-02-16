package com.five.Maeum_Eum.service.excel;

import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ExcelService {

    private final CenterRepository centerRepository;

    public void saveCenterData(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            DataFormatter dataFormatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;
                if(getCellValueAsString(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).isEmpty()) break;

                Cell centerCode = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell centerName = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell zipCode = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell address = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell designatedTime = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell installationTime = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell detailAddress = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                Center center = Center.builder()
                        .centerCode(dataFormatter.formatCellValue(centerCode))
                        .centerName(getCellValueAsString(centerName))
                        .address(getCellValueAsString(address))
                        .designatedTime(LocalDate.parse(getCellValueAsString(designatedTime)))
                        .detailAddress(getCellValueAsString(detailAddress))
                        .zipCode(getCellValueAsString(zipCode))
                        .installationTime(LocalDate.parse(getCellValueAsString(installationTime)))
                        .build();

                centerRepository.save(center);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.CENTER_NOT_FOUND);
        }
    }

    private String getCellValueAsString(Cell cell) {
        System.out.println("[LOG]"+cell+cell.getCellType());
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}
