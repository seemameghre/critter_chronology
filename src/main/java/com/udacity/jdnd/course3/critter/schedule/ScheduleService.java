package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule createSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForEmployee(Employee employee){
        return scheduleRepository.findAllByEmployeesContaining(employee);
    }
    public List<Schedule> getScheduleForPet(Pet pet){
        return scheduleRepository.findAllByPetsContaining(pet);
    }

    public List<Schedule> getScheduleForAllPets(List<Pet> pets){
        List<Schedule> schedules = new ArrayList<>();
        for (Pet pet: pets) {
            schedules.addAll(getScheduleForPet(pet));
        }
        return schedules;
    }
}
