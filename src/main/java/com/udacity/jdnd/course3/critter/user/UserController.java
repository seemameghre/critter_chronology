package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer savedCustomer = customerService.save(convertCustomerDTOToEntity(customerDTO));
        return convertCustomerEntityToDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.findAll().stream().map(c -> convertCustomerEntityToDTO(c)).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = customerService.getOwnerForPet(petService.getPet(petId));
        return convertCustomerEntityToDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
       Employee employee = convertEmployeeDTOToEntity(employeeDTO);
       return convertEmployeeEntityToDTO(employeeService.create(employee));
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeEntityToDTO(employeeService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.update(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO.getSkills(),employeeDTO.getDate().getDayOfWeek());
        return employees.stream().map(e -> convertEmployeeEntityToDTO(e)).collect(Collectors.toList());
    }
    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        List<Pet> pets = new ArrayList<>();
        BeanUtils.copyProperties(customerDTO, customer);
        if(customerDTO.getPetIds() != null){
            pets = customerDTO.getPetIds().stream().map(id -> petService.getPet(id)).collect(Collectors.toList());
            customer.setPets(pets);
        }
        return customer;
    }
    private CustomerDTO convertCustomerEntityToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        List<Long> petIds;
        BeanUtils.copyProperties(customer,customerDTO);
        if(customer.getPets() != null){
            petIds = customer.getPets().stream().map(pet -> pet.getId()).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }
    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return employee;
    }
}
