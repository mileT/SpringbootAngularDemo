package com.petstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Created by jhan on 1/13/2017.
 */
@RestController
@RequestMapping("/{userId}/pets")
public class PetRestController {
    private final PetRepository petRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public PetRestController(PetRepository petRepository, AccountRepository accountRepository) {
        this.petRepository = petRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Pet> listAllPets(@PathVariable String userId) {
        this.validateUser(userId);
        return this.petRepository.findByAccountUsername(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addPet(@PathVariable String userId, @RequestBody Pet pet) {
        this.validateUser(userId);

        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    Pet result = petRepository.save(new Pet(account,
                            pet.name, pet.category));

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).build();

                })
                .orElse( ResponseEntity.noContent().build());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{petId}")
    Pet getPet(@PathVariable String userId, @PathVariable Long petId) {
        this.validateUser(userId);
        return this.petRepository.findOne(petId);
    }


    // validate user
    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}
