package tn.esprit.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.services.EntrepriseServiceImpl;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntrepriseServiceImplTest {


    @Autowired
    EntrepriseServiceImpl entrepriseService;

    private static final Logger l = LogManager.getLogger(TimesheetServiceImplTest.class);

    @Test
    public void testDeleteEntreprise(){
        int id=entrepriseService.ajouterEntreprise(new Entreprise("THE FIVE","Vente"));

        Entreprise entreprise = entrepriseService.deleteEntrepriseById(id);
assertNotNull(entreprise);
        if (entrepriseService.getEntrepriseById(entreprise.getId())==null)
            l.info("Entreprise deleted successfully");
        else
            l.error("Delete failed!");
    }

    @Test
    public void testDepartementEntreprise(){
        int idEntreprise=entrepriseService.ajouterEntreprise(new Entreprise("THE FIVE","Vente"));
        int idDepartement=entrepriseService.ajouterDepartement(new Departement("D1"));
        entrepriseService.affecterDepartementAEntreprise(idDepartement,idEntreprise);
assertNotNull(idDepartement);
        if (entrepriseService.getDepartementAtEntreprise(idDepartement,idEntreprise))
            l.info("Department Affected Successfully");
        else
            l.error("Department Not Affected");
    }


}
