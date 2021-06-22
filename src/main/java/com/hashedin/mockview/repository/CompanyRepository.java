package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
}
