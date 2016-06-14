#Assignment project

##Use
Use Spring boot to launch app

execute: **mvn spring-boot:run**

## URLs 
###Employee
- http://localhost:8080/employee/{id}
- http://localhost:8080/employee/{id}/delete
- http://localhost:8080/employee/{id}/create?
	- name
	- manager
	- department
	- salary
- http://localhost:8080/employee/{id}/update?
	- name
	- managerName
	- departmentName
	- salary
- http://localhost:8080/employee/{id}/update_salary?
	- salary
### Department
- http://localhost:8080/department/{id}
- http://localhost:8080/department/create?
	- name
	- salary_min
	- salary_max
###Swagger
- localhost:8080/swagger-ui.html

##Development

- Eclipse was used to develop this application.
- Uses Java 1.6


##Description of classes

-  App: Contains main method to launch application.
-  SwaggerConfig: swagger utility class to configure it.
- Department: Entity class.
- Employee: Entity class.
- DepartmentRepository: Spring data repository.
- EmployeeRepository: Spring data repository.
- DepartmentController: Services for department abstraction.
- EmployeeController: Services for employee abstraction.

##Description of assignment
 
Coding assignment for you from Hiring manager for  Verizon Telecom REQ# VZTJP00036842, Core Java Developer, Verizon Telecom, Temple Terrace, FL (or) Basking Ridge, NJ position.
 
 
Listed out the problem statement for coding assignment.   
 
Create Tables with index and foreign keys: (any RDBMS)
 
Employee ( id, name , manager_name, dept , salary)
Department (id, name , salar_min_range, salary_max_Range)
 
Write  2 Spring boot app
 
1)  Employee App
 
1. Get Employee details along with Department using employee id
2. Update Employee details (Validate Department/salary using department app REST call , manager name using same employee table)
3. Update Salary
4. Create new employee (Validate Department/salary using department app REST call , , manager name using same employee table)
5. Delete employee
 
2) Department App

1.  Get Department Details
2.  Create Department
 
with
 
1. Validate the input for all the functions
2. Define swagger page
3. Define exception advise
4. use Spring Data JPA for DB operations
5. Define Transaction scope
6. Junit test cases and code coverage

Note:  expecting to be working code as writing Junit will not make sense on a sample.
