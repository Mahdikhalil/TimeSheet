package tn.esprit.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.ITimesheetService;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
        }
        Departement departement = new Departement("D1");
        dr.save(departement);
        l.info("departement created and added");
        Mission mission = mr.findById(idm).get();
        tss.affecterMissionADepartement(mission.getId(),departement.getId());
        l.info("Mission affected");
//        Optional<Mission> missionFetched = departement.getMissions().stream().filter(mission1 -> mission1.getId() == idm).findFirst();

        assertEquals(departement.getMissions().stream().filter(m -> m.getId() == idm).findFirst().get().getId(), idm);

//        if( missionFetched.get().getId() == idm){
//            l.info("Mission found");
//        }else{
//            l.warn("warning check your method");
//        }

    }

    @Test
    public void testAjouterTimesheet() throws ParseException {

        int idm = tss.ajouterMission(new Mission("M1","D1"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2015-03-23");
        Date date2 = dateFormat.parse("2015-03-23");

        tss.ajouterTimesheet(idm,0,date1,date2);

        l.info("timesheet parsed");

        Timesheet timesheet = (Timesheet) tsr.getTimesheetsByMissionAndDate(mr.findById(idm).get(),date1,date2);
        if (timesheet.isValide()) {
            l.info("TimeSheet Added");
        }else{
            l.error("check your code");
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
