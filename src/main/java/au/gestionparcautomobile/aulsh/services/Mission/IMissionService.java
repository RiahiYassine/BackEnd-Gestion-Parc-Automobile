package au.gestionparcautomobile.aulsh.services.Mission;

import au.gestionparcautomobile.aulsh.entities.Employe;
import au.gestionparcautomobile.aulsh.entities.Mission;
import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.records.EmployeRequest;
import au.gestionparcautomobile.aulsh.records.MissionFilter;
import au.gestionparcautomobile.aulsh.records.MissionRequest;
import au.gestionparcautomobile.aulsh.records.VehiculeFilter;

import java.util.List;

public interface IMissionService {

    Mission createMission(MissionRequest missionRequest);
    void deleteMission(Long id);
    Mission updateMission(Long id,MissionRequest missionRequest);
    Mission getMissionById(Long id);

    List<Mission> getAllMissions();

    List<String> getAllReferences();
    List<String> getAllDestinations();
    List<String> getAllMatricules();
    List<String> getAllDepartements();
    List<String> getAllResponsable();
    List<String> getAllChauffeur();


    List<Mission> getAllMissionsByDepartement(Long id);
    List<Mission> filterMissions(MissionFilter filter);
    List<Mission> filterMissionDepartement(Long id,MissionFilter filter);

    List<Mission> getAllMissionsAccepter();
    List<Mission> getAllMissionsRefuser();


}
