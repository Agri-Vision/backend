package com.research.agrivision.api.adapter.api.controller;

import com.research.agrivision.api.adapter.api.response.CommonResponse;
import com.research.agrivision.api.adapter.api.response.OrganizationResponse;
import com.research.agrivision.business.entity.IotReading;
import com.research.agrivision.business.entity.Organization;
import com.research.agrivision.business.port.in.IotUseCase;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/iot")
public class IotController {
    private final IotUseCase iotService;
    private final ModelMapper modelMapper = new ModelMapper();

    public IotController(IotUseCase iotService) {
        this.iotService = iotService;
    }

    @PostMapping("/enviroment_data")
    public ResponseEntity<IotReading> createIotReading(
            @RequestParam (required = false) String temperature,
            @RequestParam (required = false) String humidity,
            @RequestParam (required = false) String uvLevel,
            @RequestParam (required = false) String soilMoisture,
            @RequestParam (required = false) String pressure,
            @RequestParam (required = false) String altitude,
            @RequestParam (required = false) LocalDate recordedDate,
            @RequestParam (required = false) LocalTime recordedTime
    ) {

        IotReading iotReading = new IotReading();
        iotReading.setTemperature(temperature);
        iotReading.setHumidity(humidity);
        iotReading.setUvLevel(uvLevel);
        iotReading.setSoilMoisture(soilMoisture);
        iotReading.setPressure(pressure);
        iotReading.setAltitude(altitude);
        iotReading.setRecordedDate(recordedDate);
        iotReading.setRecordedTime(recordedTime);

        return ResponseEntity.ok(iotService.createIotReading(iotReading));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IotReading> getIotReadingById(@PathVariable Long id) {
        IotReading iotReading = iotService.getIotReadingById(id);
        if (iotReading == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(iotReading);
    }

    @GetMapping()
    public ResponseEntity<List<IotReading>> getAllIotReadings() {
        List<IotReading> iotReadingList = iotService.getAllIotReadings();
        if (iotReadingList == null || iotReadingList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(iotReadingList);
    }

    @PostMapping("/bulk")
    public ResponseEntity<CommonResponse> createIotReadingBulk(@RequestParam MultipartFile file) {
        List<IotReading> iotReadingList = this.extractIotReadings(file);
        if (iotReadingList == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        for (IotReading iotReading : iotReadingList) {
            try {
                iotService.createIotReading(iotReading);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new CommonResponse("Success"));
    }

    private List<IotReading> extractIotReadings(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
                throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
            }

            InputStream is = file.getInputStream();
            List<IotReading> iotReadingList = new ArrayList<>();
            Workbook workBook = WorkbookFactory.create(is);
            Sheet sheet = workBook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                IotReading iotReading = new IotReading();
                String temperature = getStringCellValue(row.getCell(1));
                String humidity = getStringCellValue(row.getCell(2));
                String uvLevel = getStringCellValue(row.getCell(3));
                String soilMoisture = getStringCellValue(row.getCell(4));
                String pressure = getStringCellValue(row.getCell(5));
                String altitude = getStringCellValue(row.getCell(6));

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm:ss");
                LocalTime recordedTime = LocalTime.parse(getStringCellValue(row.getCell(7)), timeFormatter);
                iotReading.setRecordedTime(recordedTime);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
                LocalDate recordedDate = LocalDate.parse(getStringCellValue(row.getCell(8)), formatter);
                iotReading.setRecordedDate(recordedDate);

                iotReading.setTemperature(temperature);
                iotReading.setHumidity(humidity);
                iotReading.setUvLevel(uvLevel);
                iotReading.setSoilMoisture(soilMoisture);
                iotReading.setPressure(pressure);
                iotReading.setAltitude(altitude);
                iotReading.setRecordedTime(recordedTime);
                iotReading.setRecordedDate(recordedDate);

                iotReadingList.add(iotReading);
            }

            return iotReadingList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return new DataFormatter().formatCellValue(cell);
        } else {
            return cell.getStringCellValue();
        }
    }

}
