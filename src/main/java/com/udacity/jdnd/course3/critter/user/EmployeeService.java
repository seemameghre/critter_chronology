package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployee(long employeeId){
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        Employee employee = optional.orElseThrow(EmplyeeNotFoundException::new);
        return employee;
    }
    public Employee create(Employee employee){
        return employeeRepository.save(employee);
    }

    public void update(Set<DayOfWeek> daysAvailable, long employeeId){
        Employee employee = getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }
    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek day){
        //Get all employees available on given day
        List<Employee> employees = employeeRepository.findAllByDaysAvailableContaining(day);
        //Filter by skill
        return employees.stream().filter(e -> e.getSkills().containsAll(skills)).collect(Collectors.toList());
    }
    public List<Employee> getAllEmployeesByIdIn(List<Long> employeeIds){
        return employeeRepository.findByIdIn(employeeIds);
    }
}
