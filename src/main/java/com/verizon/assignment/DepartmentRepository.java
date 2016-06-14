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
public interface DepartmentRepository extends CrudRepository<Department, Long> {

	List<Department> findByName(String name);
}
