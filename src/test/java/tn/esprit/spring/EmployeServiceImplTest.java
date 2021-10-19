package tn.esprit.spring;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.EmployeServiceImpl;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeServiceImplTest {









    private static final Logger l = LogManager.getLogger(TimesheetServiceImplTest.class);

    EmployeRepository employeRepository;

    DepartementRepository deptRepoistory;

    ContratRepository contratRepoistory;

    TimesheetRepository timesheetRepository;
    EmployeServiceImpl employeService;



    public EmployeServiceImplTest(EmployeServiceImpl employeService,EmployeRepository employeRepository,DepartementRepository deptRepoistory,ContratRepository contratRepoistory,TimesheetRepository timesheetRepository){
        this.employeRepository=employeRepository;
        this.deptRepoistory=deptRepoistory;
        this.contratRepoistory=contratRepoistory;
        this.timesheetRepository=timesheetRepository;
        this.employeService=employeService;
    }


    @Test
    public void testAddEmployeen(){
        int nbemp=employeService.getAllEmployes().size();

        int id = employeService.ajouterEmploye(new Employe("karim","slaimi","k.sleimi@gmail.com",true, Role.TECHNICIEN));

        Assert.assertNotEquals(id,0);

        int nbemp2=employeService.getAllEmployes().size();

        Assert.assertNotEquals(nbemp,nbemp2);
        l.info("mission added " + id);
    }

    @Test
    public void testAffecterContratEmp(){

        int idc=employeService.ajouterContrat(new Contrat(new Date(),"CDI",2000));


         if (idc > 0){
            l.info("contrat added");
        }

        int id = employeService.ajouterEmploye(new Employe("karim","slaimi","k.sleimi@gmail.com",true, Role.TECHNICIEN));
        if (idc > 0){
            l.info("employee added");
        }

        int idcont=employeService.affecterContratAEmploye(idc,id);

        Assert.assertNotEquals(idcont,0);

        List<Contrat> contrats=(List<Contrat>)contratRepoistory.findAll();
        Contrat fetchedContract= (Contrat) contrats.stream().filter(x->x.getReference()==idcont);

        if( idc == fetchedContract.getReference()){
            l.info("contract found");
        }else{
            l.warn("warning check your method");
        }

    }

    @Test
    public void testAffecterEmpDep()  {

        int id = employeService.ajouterEmploye(new Employe("karim","slaimi","k.sleimi@gmail.com",true, Role.TECHNICIEN));
        Assert.assertNotEquals(id,0);
        l.info("Employee added");

        int iddep=deptRepoistory.save(new Departement("IT Departement")).getId();

        Assert.assertNotEquals(iddep,0);
        l.info("Departement added");


        employeService.affecterEmployeADepartement(id,iddep);
        Employe emp=employeRepository.findById(id).orElse(new Employe());
        assertTrue(emp.getDepartements().size()>0);

        l.info("employee added to department");

        Departement dep=deptRepoistory.findById(iddep).orElse(new Departement());
        assertTrue(dep.getEmployes().stream().anyMatch(x->x.getId()==id));

        l.info("employee added to department");


        if (dep.getEmployes().stream().anyMatch(x->x.getId()==id)) {
            l.info("employee added to department");
        }else{
            l.error("error");
        }

    }

}
