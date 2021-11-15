package tn.esprit.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.EmployeServiceImpl;
import tn.esprit.spring.services.ITimesheetService;

import javax.swing.text.html.Option;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static junit.framework.Assert.*;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimesheetServiceImplTest {

    @Autowired
    ITimesheetService tss;
    @Autowired
    MissionRepository mr;
    @Autowired
    TimesheetRepository tsr;
    @Autowired
    DepartementRepository dr;
    @Autowired
    EmployeServiceImpl esi;

    private static final Logger l = LogManager.getLogger(TimesheetServiceImplTest.class);

    @Test
    public void testAddMission(){
        int id = tss.ajouterMission(new Mission("M1","D1"));
        assertNotNull(id);
        l.info("mission added " + id);
    }

    @Test
    public void testAffecterMissionADepartement(){

        int idm = tss.ajouterMission(new Mission("M1",null));
        if (idm > 0){
            l.info("mission added");
        }else{
            l.error("adding mission failed");
        }
        Departement departement = new Departement("D1");
        dr.save(departement);
        l.info("departement created and added");
        Optional<Mission> mission = mr.findById(idm);
        if(mission.isPresent()){
            l.info("mission found");
            tss.affecterMissionADepartement(mission.get().getId(),departement.getId());
            l.info("Mission affected");
            List<Departement> ld = null ;
            ld =
                    StreamSupport.stream(dr.findAll().spliterator(), false)
                            .collect(Collectors.toList());
            assertNotNull(ld);
        }

    }

    @Test
    public void testAjouterTimesheet() throws ParseException {

        int idm = tss.ajouterMission(new Mission("M1","D1"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2015-03-23");
        Date date2 = dateFormat.parse("2015-03-23");
        Employe employe= new Employe("mahdi","khalil","mahdi@mm.com",true, Role.INGENIEUR);
        tss.ajouterTimesheet(idm,esi.ajouterEmploye(employe),date1,date2);

        l.info("timesheet parsed");
        l.info("webhook");
        if(mr.findById(idm).isPresent()){
            assertNotNull(tsr.getTimesheetsByMissionAndDate(mr.findById(idm).get(),date1,date2));
        }
    }

    @Test
    public void testDeleteTimesheet() throws ParseException {

        int idm = tss.ajouterMission(new Mission("M1","D1"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2015-03-23");
        Date date2 = dateFormat.parse("2015-03-23");
        Employe employe= new Employe("mohamed","khalil","med@mm.com",true, Role.INGENIEUR);
        tss.ajouterTimesheet(idm,esi.ajouterEmploye(employe),date1,date2);
        Timesheet ts = tsr.findBytimesheetPK(new TimesheetPK(idm,employe.getId(),date1,date2));
        assertNotNull(ts);
        l.info("ts added and found ");
        tsr.delete(ts);
        ts = tsr.findBytimesheetPK(new TimesheetPK(idm,employe.getId(),date1,date2));
        l.info("ts deleted");
        assertNull(ts);
    }

    @Test
    public void testGetAllMission(){
        List<Mission> missions =
                StreamSupport.stream(mr.findAll().spliterator(), false)
                        .collect(Collectors.toList());
        l.info("get all missions");
        assertTrue(missions.size()>0);
    }

    @Test
    public void testUpdateMission(){
        int id = tss.ajouterMission(new Mission("MissionBefore","D1"));
        assertNotNull(id);
        l.info("mission added " + id);
        Optional<Mission> mission = mr.findById(id);
        if(mission.isPresent()){
            mission.get().setName("missionAfter");
            mr.save(mission.get());
            l.info("mission updated");
            assertNotSame(mission.get().getName(),"MissionBefore");
        }

    }







    public void timeTest() throws ParseException {

        long start1 = System.nanoTime();
        testAddMission();
        long end1 = System.nanoTime();
        System.out.println("test add mission take Time in nano seconds: "+ (end1-start1));

        long start2 = System.nanoTime();
        testAffecterMissionADepartement();
        long end2 = System.nanoTime();
        System.out.println("test affect mission to departement Time in nano seconds: "+ (end2-start2));

        long start3 = System.nanoTime();
        testAjouterTimesheet();
        long end3 = System.nanoTime();
        System.out.println("test add Timesheet Time in nano seconds: "+ (end3-start3));

    }

    @Around("execution(* tn.esprit.spring.service.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        if(elapsedTime > 0 ){
            System.out.println(pjp + " took "+ elapsedTime + "MS");
        }
        System.out.println("Method execution time: " + elapsedTime + " milliseconds.");
        return obj;
    }
}
