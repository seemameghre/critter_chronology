package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepository extends UserRepository<Employee> {

    public List<Employee> findByIdIn(List<Long> employeeIds);
    public List<Employee> findAllByDaysAvailableContaining(@Param("day")DayOfWeek day);
}
