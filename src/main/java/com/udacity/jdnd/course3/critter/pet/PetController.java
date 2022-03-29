package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO)  {
        Pet pet = copyPetDTOToPet(petDTO);
        Pet savedPet = petService.savePet(pet);
        return copyPetToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return copyPetToPetDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getPets().stream().map(p -> copyPetToPetDTO(p)).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer customer = customerService.findById(ownerId);
        return petService.getPetsByOwner(customer).stream().map(p -> copyPetToPetDTO(p)).collect(Collectors.toList());
    }

    private Pet copyPetDTOToPet(PetDTO petDTO) throws CustomerNotFoundException {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        if((Long)petDTO.getOwnerId() != null){
            //get customer from id and set in pet
            pet.setCustomer(customerService.findById(petDTO.getOwnerId()));
        }
        return pet;
    }
    private PetDTO copyPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }
}
