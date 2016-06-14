package com.verizon.assignment;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author David Flores
 *
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String name;
	Double salaryMin;
	Double salaryMax;
	@OneToMany(targetEntity=Employee.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Collection<Employee> employees;
	
	protected Department() {}
	
	public Department(final String name, final Double salaryMin, final Double salaryMax) {
		this.name = name;
		this.salaryMin = salaryMin;
		this.salaryMax = salaryMax;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(Double salaryMin) {
		this.salaryMin = salaryMin;
	}

	public Double getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(Double salaryMax) {
		this.salaryMax = salaryMax;
	}
	
	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}

	public String toString() {
		return String.format(
                "Department [id=%d, Name='%s', salary min='%f', salary max='%f']",
                id, name, salaryMin, salaryMax);
	}
}
