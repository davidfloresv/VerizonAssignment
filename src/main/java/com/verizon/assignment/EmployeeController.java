/**
 * 
 */
package com.verizon.assignment;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for assignment 1) Employee App
 * 
 * @author David Flores
 *
 */
@RestController
@EnableAutoConfiguration
public class EmployeeController {

	@Resource
	private DepartmentRepository departmentRepository;
	@Resource
	protected EmployeeRepository employeesRepository;

	/**
	 * Pre-loads data
	 */
	@Bean
	@Transactional
	public EmployeeRepository loadData(DepartmentRepository departmentRepo,
			EmployeeRepository employeeRepo) {
		final Department owner = new Department("owners", 1.0, 10.0);
		departmentRepo.save(owner);
		final Department managers = new Department("managers", 20.0, 50.0);
		departmentRepo.save(managers);
		departmentRepo.save(new Department("resources", 100.0, 200.0));
		for (Department department : departmentRepo.findAll()) {
			System.out.println(department);
		}

		Employee tmpEmployee;

		tmpEmployee = new Employee();
		tmpEmployee.setName("Zack");
		tmpEmployee.setDepartment(owner);
		tmpEmployee.setSalary(1.00);
		employeeRepo.save(tmpEmployee);

		tmpEmployee = new Employee();
		tmpEmployee.setName("Sara");
		tmpEmployee.setDepartment(owner);
		tmpEmployee.setSalary(1.00);
		employeeRepo.save(tmpEmployee);

		tmpEmployee = new Employee();
		tmpEmployee.setName("Tom");
		tmpEmployee.setDepartment(managers);
		tmpEmployee.setManagerName("Sara");
		tmpEmployee.setSalary(30.00);
		employeeRepo.save(tmpEmployee);

		for (Employee employee : employeeRepo.findAll()) {
			System.out.println(employee);
		}
		return employeeRepo;
	}

	/**
	 * From assignment <br>
	 * 1) Get Employee details along with Department using employee id
	 */
	@RequestMapping(value="/employee/{id}", method = RequestMethod.GET)
	@Transactional
	public String getDetails(@PathVariable String id) {
		validateId(id);
		return findEmployeeById(id).toString();
	}

	/**
	 * From assignment <br>
	 * 2) Update Employee details (Validate Department/salary using department
	 * app REST call , manager name using same employee table)
	 */
	@RequestMapping(value="/employee/{id}/update")
	@Transactional
	public String updateDetails(
			@PathVariable String id,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "managerName", defaultValue = "") String managerName,
			@RequestParam(value = "departmentName", defaultValue = "") String departmentName,
			@RequestParam(value = "salary", defaultValue = "0.0") String salary) {
		validateId(id);
		final Employee employee = findEmployeeById(id);
		if (managerName.trim().length() > 0) {
			validateManagerName(managerName);
			employee.setManagerName(managerName);
		}
		final Department department;
		if (departmentName.trim().length() > 0) {
			department = findDepartmentByName(departmentName);
			employee.setDepartment(department);
		} else {
			department = employee.getDepartment();
		}
		Double valueSalary = new Double(salary);
		validateDepartmentSalary(department, valueSalary);
		employee.setSalary(valueSalary);
		employeesRepository.save(employee);
		return employee.toString();
	}

	/**
	 * From assignment <br>
	 * 3) Update Salary
	 */
	@RequestMapping(value="/employee/{id}/update_salary")
	@Transactional
	public String updateSalary(
			@PathVariable String id,
			@RequestParam(value = "salary", defaultValue = "0.0") String salary) {
		validateId(id);
		final Employee employee = findEmployeeById(id);
		Double valueSalary = new Double(salary);
		final Department department = employee.getDepartment();
		validateDepartmentSalary(department, valueSalary);
		employee.setSalary(valueSalary);
		employeesRepository.save(employee);
		return employee.toString();
	}

	/**
	 * From assignment <br>
	 * 4) Create new employee (Validate Department/salary using department app
	 * REST call , , manager name using same employee table)
	 */
	@RequestMapping(value="/employee/create")
	@Transactional
	public String create(
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "manager", defaultValue = "") String managerName,
			@RequestParam(value = "department", defaultValue = "") String departmentName,
			@RequestParam(value = "salary", defaultValue = "0.0") String salary) {
		validateManagerName(managerName);
		final Department department = findDepartmentByName(departmentName);
		Double valueSalary = new Double(salary);
		validateDepartmentSalary(department, valueSalary);
		Employee tmpEmployee = new Employee();
		tmpEmployee.setName(name);
		tmpEmployee.setManagerName(managerName);
		tmpEmployee.setDepartment(department);
		tmpEmployee.setSalary(valueSalary);
		employeesRepository.save(tmpEmployee);
		return tmpEmployee.toString();
	}

	/**
	 * From assignment <br>
	 * 5) Delete employee
	 **/
	@RequestMapping(value="/employee/{id}/delete")
	@Transactional
	public String delete(@PathVariable String id) {
		validateId(id);
		final Employee employee = findEmployeeById(id);
		employeesRepository.delete(employee);
		return employee.toString();
	}

	/**
	 * Validates an id.
	 * 
	 * @param id
	 *            Value to check
	 * @throws EmployeeNotFoundException
	 *             if the Id is less than zero or not a number.
	 */
	private void validateId(final String id) {
		try {
			final Long value = new Long(id);
			if (value <= 0) {
				throw new EmployeeNotFoundException(id);
			}
		} catch (NumberFormatException e) {
			throw new EmployeeNotFoundException(id);
		}
	}

	/**
	 * Validates the manager names
	 * 
	 * @param managerName
	 *            Value to check
	 * @throws ManagerNotFoundException
	 *             if the manager is not an employee.
	 */
	private void validateManagerName(final String managerName) {
		final List<Employee> managers = employeesRepository
				.findByName(managerName);
		if (managers.isEmpty()) {
			throw new ManagerNotFoundException(managerName);
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
		String valueId = department.getId().toString();
		String valueSalary = salary.toString();
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/department/{id}/check_salary?salary={salary}").buildAndExpand(valueId,valueSalary).toUriString();
		System.out.println("calling: " + url);
		RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
	    System.out.println("response: " + response.getBody());
	    Boolean accepted = Boolean.valueOf(response.getBody().toString());
	    if(accepted == false) {
	    	throw new SalaryRangeException(salary);
	    }
	    return accepted;
	}

	/**
	 * Finds an employee.
	 * 
	 * @param id
	 *            Id to search
	 * @return Returns an employee.
	 * @throws EmployeeNotFoundException
	 *             if employee is not found.
	 */
	private Employee findEmployeeById(final String id) {
		final Long valueId = new Long(id);
		final Employee employee = employeesRepository.findOne(valueId);
		if (employee == null) {
			throw new EmployeeNotFoundException(id);
		}
		return employee;
	}

	/**
	 * Finds a department.
	 * 
	 * @param name
	 *            Name to search
	 * @return Returns the department.
	 * @throws DepartmentNameNotFoundException
	 *             if the department is not found.
	 */
	private Department findDepartmentByName(final String name) {
		final List<Department> departments = departmentRepository
				.findByName(name);
		if (departments.isEmpty()) {
			throw new DepartmentNameNotFoundException(name);
		}
		return departments.get(0);
	}
}

@ControllerAdvice
class EmployeeControllerAdvice {

	@ResponseBody
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String employeeNotFound(String id) {
		return "Can't find Employee Id: " + id;
	}
}

class EmployeeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployeeNotFoundException(final String id) {
		super("Can't find Employee Id: " + id + ".");
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class DepartmentNameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DepartmentNameNotFoundException(final String departmentName) {
		super("Can't find department: " + departmentName + ".");
	}
}

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
class ManagerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ManagerNotFoundException(final String managerName) {
		super("Can't find Manager with name: " + managerName + ".");
	}
}

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
class SalaryRangeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SalaryRangeException(final double salary) {
		super("Can't have the salary: " + salary + ".");
	}
}
