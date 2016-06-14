/**
 * 
 */
package com.verizon.assignment;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * @author David Flores
 *
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	List<Employee> findByName(String name);
}
