package com.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

}
