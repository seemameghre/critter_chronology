package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(long customerId) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if(optional.isPresent())
            return optional.get();
        else
            throw new CustomerNotFoundException();
    }
    public Customer getOwnerForPet(Pet pet){ return customerRepository.findCustomerByPet(pet);}
}
