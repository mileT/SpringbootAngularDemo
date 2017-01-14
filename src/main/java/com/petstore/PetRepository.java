package com.petstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by jhan on 1/13/2017.
 */
public interface PetRepository extends JpaRepository<Pet, Long>{
    Collection<Pet> findByAccountUsername(String username);
}
