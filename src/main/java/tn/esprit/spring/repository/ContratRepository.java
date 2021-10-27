package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;

public interface ContratRepository extends CrudRepository<Contrat, Integer>{
    @Query("select c from Contrat c left JOIN fetch c.employe where c.id=:id")
    public Departement ContratById(@Param("id")int contId);
} 
