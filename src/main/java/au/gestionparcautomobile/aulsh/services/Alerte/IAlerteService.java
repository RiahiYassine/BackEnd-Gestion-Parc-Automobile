package au.gestionparcautomobile.aulsh.services.Alerte;

import au.gestionparcautomobile.aulsh.entities.Alerte;
import au.gestionparcautomobile.aulsh.enums.AlerteStatus;
import au.gestionparcautomobile.aulsh.records.AlerteFilter;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IAlerteService {


    Alerte createAlerte(Alerte alerte);
    Alerte updateAlerte(Long id,Alerte alerte);
    Alerte getAlerte(Long id);
    void deleteAlerte(Long id);
    List<Alerte> getAllAlertes();


    List<Alerte> getAlertesByDateReminder();
    void checkAndSendNotifications();

    List<Alerte> filterAlertesEnCour(AlerteFilter filter);
    List<Alerte> filterAlertesDone(AlerteFilter filter);

    List<String> getAllTypeAlertes();

    List<String> getAllAlertesMatricules();

    Alerte finishAlerte(Long id);

    List<Alerte> getAllAlerteByStatus(AlerteStatus status);

    long countAlertes();
}
