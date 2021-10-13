package tn.esprit.spring;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntrepriseServiceImplTest {


    @Autowired
    IEntrepriseService es ;
    @Autowired
    EntrepriseRepository entrepriseRepoistory;
    @Autowired
    DepartementRepository deptRepoistory;

/*
    private static final Logger l = LogManager.getLogger(TimesheetServiceImplTest.class);

    @Test
    public void testAjouterEntreprise() {

        int entreprise = es.ajouterEntreprise(new Entreprise("entreprise1","entreprise2"));
        assertNotNull(id);
        l.info("entreprise ajouté " + entreprise);

    }

    @Test
    public void testAjouterDepartement(){

        int dept = es.ajouterDepartement(new Departement("dept1","dept2"));
        assertNotNull(id);
        l.info("departement ajouté " + dept);
    }
    */
}
