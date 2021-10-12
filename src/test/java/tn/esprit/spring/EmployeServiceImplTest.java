package tn.esprit.spring;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.*;
import tn.esprit.spring.services.EmployeServiceImpl;
import tn.esprit.spring.services.ITimesheetService;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

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
    public void testAddEmployeen(){
        int nbemp=employeService.getAllEmployes().size();

        int id = employeService.ajouterEmploye(new Employe("karim","slaimi","k.sleimi@gmail.com",true, Role.TECHNICIEN));

        assertNotNull(id);
        int nbemp2=employeService.getAllEmployes().size();

        assertFalse(nbemp2==nbemp);
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

        assertNotNull(idcont);

        List<Contrat> contrats=(List<Contrat>)cr.findAll();
        Contrat fetchedContract= (Contrat) contrats.stream().filter(x->x.getReference()==idcont);

        if( idc == fetchedContract.getReference()){
            l.info("Mission found");
        }else{
            l.warn("warning check your method");
        }

    }

    @Test
    public void testAffecterEmpDep() throws ParseException {

        int id = employeService.ajouterEmploye(new Employe("karim","slaimi","k.sleimi@gmail.com",true, Role.TECHNICIEN));
        assertTrue(id!=0);
        l.info("Employee added");

        int iddep=dr.save(new Departement("IT Departement")).getId();

        assertTrue(iddep!=0);
        l.info("Departement added");


        employeService.affecterEmployeADepartement(id,iddep);
        Employe emp=er.findById(id).get();
        assertTrue(emp.getDepartements().size()>0);

        l.info("employee added to department");

        Departement dep=dr.findById(iddep).get();
        assertTrue(dep.getEmployes().stream().anyMatch(x->x.getId()==id));

        l.info("employee added to department");


        if (dep.getEmployes().stream().anyMatch(x->x.getId()==id)) {
            l.info("employee added to department");
        }else{
            l.error("error");
        }

    }

}
