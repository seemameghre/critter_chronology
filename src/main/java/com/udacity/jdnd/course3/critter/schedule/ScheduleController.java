package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private PetService petService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.createSchedule(convertScheduleDTOToEntity(scheduleDTO));
        return convertScheduleEntityToDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream().map(s -> convertScheduleEntityToDTO(s)).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleForPet(petService.getPet(petId))
                .stream().map(s -> convertScheduleEntityToDTO(s))
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeService.getEmployee(employeeId))
                .stream().map(s -> convertScheduleEntityToDTO(s))
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findById(customerId);
        return scheduleService.getScheduleForAllPets(customer.getPets())
                .stream().map(s -> convertScheduleEntityToDTO(s))
                .collect(Collectors.toList());
    }
    private Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();

        //petIds and employeeIds will be ignored as property names are different
        BeanUtils.copyProperties(scheduleDTO, schedule);
        //Get list of pets from list of pet ids and set it in entity
        schedule.setPets(petService.getAllPetsByIdIn(scheduleDTO.getPetIds()));
        //Get list of employees from list of employee ids and set it in entity
        schedule.setEmployees(employeeService.getAllEmployeesByIdIn(scheduleDTO.getEmployeeIds()));
        return schedule;
    }
    private ScheduleDTO convertScheduleEntityToDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        //petIds and employeeIds will be ignored as property names are different
        BeanUtils.copyProperties(schedule, scheduleDTO);
        //Set pet id list by getting id for each pet
        scheduleDTO.setPetIds(schedule.getPets().stream().map(p -> p.getId()).collect(Collectors.toList()));
        //Set employee id list by getting employee id for each employee
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(e -> e.getId()).collect(Collectors.toList()));
        return scheduleDTO;
    }
}

