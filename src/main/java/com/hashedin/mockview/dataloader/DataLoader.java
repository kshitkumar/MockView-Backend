package com.hashedin.mockview.dataloader;

import com.hashedin.mockview.model.Company;
import com.hashedin.mockview.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class DataLoader implements ApplicationRunner {

    @Autowired
    CompanyRepository companyRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.debug("Adding mock company list");
        List<String> stringCompanyList = Arrays.asList("IBM", "Infosys", "Google", "Microsoft", "Amazon",
                "Deloitte", "HashedIn", "Oracle", "Swiggy","Paytm", "Zomato", "Paypal", "Goldman Sachs", "Apple",
                "Intel", "SAP", "American Express", "Mcafee Software", "Razorpay", "Flipkart", "Intuit", "Myntra", "Rakuten");
        List<Company> companyList = new ArrayList<>();

        for (int i = 0; i < stringCompanyList.size(); i++) {
            companyList.add(Company.builder().companyName(stringCompanyList.get(i)).build());
        }
        companyRepository.saveAll(companyList);
        log.debug("Added company data in database");
    }
}
