package au.gestionparcautomobile.aulsh.services.VehiculeSpecif;

import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;

import java.util.List;

public interface IVehiculeSpecifService {

    VehiculeSpecif createVehiculeSpecif(VehiculeSpecif vehiculeSpecif);
    VehiculeSpecif updateVehiculeSpecif(Long id, VehiculeSpecif vehiculeSpecif);
    void deleteVehiculeSpecifById(Long id);
    VehiculeSpecif getVehiculeSpecifById(Long id);

    List<VehiculeSpecif> getAllVehiculeSpecifs();


    List<String> getAllImmatriculation();

    List<String> getAllMarques();

    List<String> getAllModeles();

}
