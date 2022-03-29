package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet savePet(Pet pet){
        Pet savedPet =  petRepository.save(pet);
        Customer owner = savedPet.getCustomer();
        List<Pet> pets = new ArrayList<>();
        if(pets != null && !pets.isEmpty()){
            pets.addAll(owner.getPets());
        }
        pets.add(savedPet);
        owner.setPets(pets);
        customerRepository.save(owner);
        return savedPet;
    }
    public Pet getPet(long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow(PetNotFoundException::new);
        return pet;
    }
    public List<Pet> getPets(){
        return petRepository.findAll();
    }
    public List<Pet> getPetsByOwner(Customer customer){
        return petRepository.findAllPetsByCustomer(customer);
    }

    //Get list of pets from list of petIds
    public List<Pet> getAllPetsByIdIn(List<Long> petIds){
        return petRepository.findByIdIn(petIds);
    }
}
