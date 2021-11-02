package tn.esprit.spring;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import tn.esprit.spring.services.ITimesheetService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeServiceImplTest {

    @Autowired
    ITimesheetService tss;
    @Autowired
    ContratRepository cr;
    @Autowired
    TimesheetRepository tsr;
    @Autowired
    DepartementRepository dr;
    @Autowired
    EmployeRepository er;

    @Autowired
    EmployeServiceImpl employeService;


    private static final Logger l = LogManager.getLogger(TimesheetServiceImplTest.class);

    @Test
    public void testAddEmployeen() {
        int nbemp = employeService.getAllEmployes().size();

        int id = employeService.ajouterEmploye(new Employe("karim", "slaimi", "k.sleimi@gmail.com", true, Role.TECHNICIEN));

        Assertions.assertThat(id).isNotZero();
        int nbemp2 = employeService.getAllEmployes().size();

        assertNotEquals(nbemp2, nbemp);
        l.info("mission added " + id);
    }

    @Test
    public void testAffecterContratEmp() {

        int idc = employeService.ajouterContrat(new Contrat(new Date(), "CDI", 2000));


        if (idc > 0) {
            l.info("contrat added");
        }

        int id = employeService.ajouterEmploye(new Employe("tira", "emna", "tira.emna97@gmail.com", true, Role.TECHNICIEN));
        if (idc > 0) {
            l.info("employee added");
        }

        int idcont = employeService.affecterContratAEmploye(idc, id);

        Assert.assertNotEquals(idcont, 0);

        List<Contrat> contrats = (List<Contrat>) cr.findAll();
        Contrat fetchedContract = contrats.stream().filter(x -> x.getReference() == idcont).findFirst().orElse(new Contrat());

        if (idc == fetchedContract.getReference()) {
            l.info("contract found");
        } else {
            l.warn("warning check your method");
        }

    }

    @Test
    public void testAffecterEmpDep()  {

        Employe employee = new Employe("tira", "emna", "tira.emna97@gmail.com", true, Role.TECHNICIEN);
        int id = employeService.ajouterEmploye(employee);
        Assert.assertNotEquals(id, 0);
        l.info("Employee added");

        int iddep = dr.save(new Departement("IT Departement")).getId();

        Assert.assertNotEquals(iddep, 0);
        l.info("Departement added");


        employeService.affecterEmployeADepartement(id, iddep);
        Employe emp = er.findById(id).orElse(new Employe());
        assertTrue(emp.getDepartements().size() > 0);

        l.info("employee added to department");

        Departement dep = dr.DepartementById(iddep);
        boolean flag = dep.getEmployes().stream().anyMatch(x -> x.getId() == id);
        assertTrue(flag);

        l.info("employee added to department");


        if (dep.getEmployes().stream().anyMatch(x -> x.getId() == id)) {
            l.info("employee added to department");
        } else {
            l.error("error");
        }

    }


    // liste des employes
    @Test
    public void testgetAllEmployes() {

        List<Employe> employees = (List<Employe>) er.findAll();

        Assertions.assertThat(employees.size()).isPositive();
        l.info("Get all employees passed!" );
    }

    @Test
    public void deleteEmployeByIdTest() {

        Employe emp = new Employe("tira", "emna", "emna@tira.com", true, Role.TECHNICIEN);
        int id = employeService.ajouterEmploye(emp);
        Employe employee;
        if ((employee = er.findById(id).orElse(new Employe())).getId() != 0) {


            er.delete(employee);

            Assertions.assertThat(employee).isNotNull();
        }else {
            fail();
        }
        l.info("Employee deleted!" + id);
    }


    @Test
    public void updateEmployeeTest() {


        Employe emp = new Employe("tira", "emna", "emna@tira.com", true, Role.TECHNICIEN);
        int id = employeService.ajouterEmploye(emp);
        Employe employee;
        if ((employee = er.findById(id).orElse(new Employe())).getId() != 0) {


            employee.setEmail("emna@gmail.com");

            Employe employeeUpdated = er.save(employee);

            Assertions.assertThat(employeeUpdated.getEmail()).isEqualTo("emna@gmail.com");

        } else {
            fail();
        }
        l.info("Employee is updated! " + id);
    }

    @Test
    public void getSalaireByEmployeIdJPQLTest() {

        Employe emp = new Employe("tira", "emna", "emna@tira.com", true, Role.TECHNICIEN);
        int id = employeService.ajouterEmploye(emp);
        int idc = employeService.ajouterContrat(new Contrat(new Date(), "CDI", 2000));
        employeService.affecterContratAEmploye(idc, id);

        float employe = er.getSalaireByEmployeIdJPQL(id);
        Assertions.assertThat(employe).isEqualTo(2000);

        l.info("Passed!"+id);


    }


}




