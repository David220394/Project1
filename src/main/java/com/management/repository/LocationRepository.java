package com.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
	
	

}
