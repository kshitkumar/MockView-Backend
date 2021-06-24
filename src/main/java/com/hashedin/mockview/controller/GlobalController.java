package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.DataDto;
import com.hashedin.mockview.service.GlobalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@CrossOrigin
public class GlobalController {
    @Autowired
    GlobalService globalService;

    @GetMapping("/companies")
    public ResponseEntity<DataDto> getAllCompanies() {
        DataDto companyDto = globalService.findAllCompanies();
        return new ResponseEntity<>(companyDto, HttpStatus.OK);

    }
    @GetMapping("/industries")
    public ResponseEntity<DataDto> getAllIndustries()
    {
        DataDto industryDto = globalService.findAllIndustries();


        return new ResponseEntity<>(industryDto,HttpStatus.OK);
    }
    @GetMapping("/positions")
    public ResponseEntity<DataDto> getALlPositions(){
        DataDto positionDto = globalService.findAllPositions();
        return new ResponseEntity<>(positionDto,HttpStatus.OK);
    }
}
