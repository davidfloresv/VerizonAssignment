/**
 * 
 */
package com.verizon.assignment;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for assignment 2) Department App
 * 
 * @author David Flores
 *
 */
@RestController
@EnableAutoConfiguration
public class DepartmentController {

	@Resource
	private DepartmentRepository departmentRepository;

	/**
	 * Pre-loads data
	 */
	@Bean
	public DepartmentRepository loadData(DepartmentRepository departmentRepo) {
		departmentRepo.save(new Department("Owners", 1.0, 1.0));
		departmentRepo.save(new Department("Manager", 10.0, 50.0));
		departmentRepo.save(new Department("Resources", 100.0, 200.0));
		for (Department department : departmentRepo.findAll()) {
			System.out.println(department);
		}
		return departmentRepo;
	}

	/**
	 * From assignment <br>
	 * 1) Get Department Details
	 **/
	@RequestMapping(value="/department/{id}", method = RequestMethod.GET)
	public String getDepartmentDetails(@PathVariable String id) {
		validateId(id);
		return findDepartmentById(id).toString();
	}

	/**
	 * From assignment <br>
	 * 2) Create Department
	 **/
	@RequestMapping(value="/department/create")
	@Transactional
	public String createDepartment(
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "salary_min", defaultValue = "0.0") String salaryMin,
			@RequestParam(value = "salary_max", defaultValue = "0.0") String salaryMax) {
		Double valueSalaryMin = new Double(salaryMin);
		Double valueSalaryMax = new Double(salaryMax);
		final Department department = new Department(name, valueSalaryMin,
				valueSalaryMax);
		departmentRepository.save(department);
		return department.toString();
	}
	
	@RequestMapping(value="/department/{id}/check_salary", method = RequestMethod.GET)
	public Boolean getDepartmentSalary(@PathVariable String id,
			@RequestParam(value = "salary", defaultValue = "0.0") String salary) {
		validateId(id);
		final Department department = findDepartmentById(id);
		Double valueSalary = new Double(salary);
		return validateDepartmentSalary(department, valueSalary);
	}

	/**
	 * Validates an id
	 * 
	 * @param id
	 *            Value to check.
	 * @throws DepartmentNotFoundException
	 *             if the Id is less than zero or not a number.
	 */
	private void validateId(final String id) {
		try {
			final Long value = new Long(id);
			if (value <= 0) {
				throw new DepartmentNotFoundException(id);
			}
		} catch (NumberFormatException e) {
			throw new DepartmentNotFoundException(id);
		}
	}

	/**
	 * Validates a salary is between the salary range for the given department.
	 * 
	 * @param department
	 *            Department
	 * @param salary
	 *            Value to check
	 * @throws SalaryRangeException
	 *             if salary is out of the department's salary range.
	 */
	private boolean validateDepartmentSalary(Department department, Double salary) {
		if (department.getSalaryMin().compareTo(salary) > 0
				|| department.getSalaryMax().compareTo(salary) < 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Find the department.
	 * 
	 * @param id
	 *            Id to search
	 * @return Returns the department.
	 * @throws DepartmentNotFoundException
	 *             if the department can't be found.
	 */
	private Department findDepartmentById(final String id) {
		final Long valueId = new Long(id);
		final Department department = departmentRepository.findOne(valueId);
		if (department == null) {
			throw new DepartmentNotFoundException(id);
		}
		return department;
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class DepartmentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DepartmentNotFoundException(final String id) {
		super("Can't find department: " + id + ".");
	}
}
