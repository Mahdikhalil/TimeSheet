package tn.esprit.spring.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeServiceImpl implements IEmployeService {


	EmployeRepository employeRepository;

	DepartementRepository deptRepoistory;

	ContratRepository contratRepoistory;

	TimesheetRepository timesheetRepository;
	static String exception="return value is null at method AAA";


	public EmployeServiceImpl(EmployeRepository employeRepository,DepartementRepository deptRepoistory,ContratRepository contratRepoistory,TimesheetRepository timesheetRepository){
		this.employeRepository=employeRepository;
		this.deptRepoistory=deptRepoistory;
		this.contratRepoistory=contratRepoistory;
		this.timesheetRepository=timesheetRepository;
	}

	public int ajouterEmploye(Employe employe) {
		employeRepository.save(employe);
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Employe employe = employeRepository.findById(employeId).orElseThrow(()-> new NullPointerException(exception));
		employe.setEmail(email);
		employeRepository.save(employe);

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		Departement depManagedEntity = deptRepoistory.findById(depId).orElseThrow(()-> new NullPointerException(exception));
		Employe employeManagedEntity = employeRepository.findById(employeId).orElseThrow(()-> new NullPointerException(exception));

		if(depManagedEntity.getEmployes() == null){

			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
		}else{

			depManagedEntity.getEmployes().add(employeManagedEntity);

		}

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		Departement dep = deptRepoistory.findById(depId).orElseThrow(()-> new NullPointerException(exception));

		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
				dep.getEmployes().remove(index);
				break;//a revoir
			}
		}
	}

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public int affecterContratAEmploye(int contratId, int employeId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElseThrow(()-> new NullPointerException(exception));
		Employe employeManagedEntity = employeRepository.findById(employeId).orElseThrow(()-> new NullPointerException(exception));

		contratManagedEntity.setEmploye(employeManagedEntity);
		return contratRepoistory.save(contratManagedEntity).getReference();
		
	}

	public String getEmployePrenomById(int employeId) {
		Employe employeManagedEntity = employeRepository.findById(employeId).orElseThrow(()-> new NullPointerException(exception));
		return employeManagedEntity.getPrenom();
	}
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).orElseThrow(()-> new NullPointerException(exception));

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElseThrow(()-> new NullPointerException(exception));
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
				return (List<Employe>) employeRepository.findAll();
	}

}
