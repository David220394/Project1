package com.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="LOCATION")
public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToMany(mappedBy = "location")
	private List<Consultation> consultations;

	
	
	public Location() {
		super();
	}

	public Location(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Consultation> getConsultations() {
		return consultations;
	}

	public void setConsultations(List<Consultation> consultations) {
		this.consultations = consultations;
	}
	
	

}
