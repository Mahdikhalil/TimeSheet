package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Departement;

public interface DepartementRepository extends CrudRepository<Departement, Integer>{
    @Query("select d from Departement d left JOIN fetch d.employes where d.id=:id")
    public Departement DepartementById(@Param("id")int depId);

}
