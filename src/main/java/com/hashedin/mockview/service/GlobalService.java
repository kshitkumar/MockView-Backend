package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.DataDto;
import com.hashedin.mockview.model.Company;
import com.hashedin.mockview.model.Industry;
import com.hashedin.mockview.model.Position;
import com.hashedin.mockview.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GlobalService {

    @Autowired
    CompanyRepository companyRepository;

    public DataDto findAllCompanies() {
        List<Company> companyList = companyRepository.findAll();
        List<String> stringList = companyList.stream().map(x -> x.getCompanyName()).collect(Collectors.toList());
        return DataDto.builder()
                .values(stringList)
                .build();
    }


    public DataDto findAllIndustries() {
        List<String> industryList = Arrays.stream(Industry.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
        return DataDto.builder()
                .values(industryList)
                .build();

    }

    public DataDto findAllPositions() {
        List<String> industryList = Arrays.stream(Position.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
        return DataDto.builder()
                .values(industryList)
                .build();
    }
}
