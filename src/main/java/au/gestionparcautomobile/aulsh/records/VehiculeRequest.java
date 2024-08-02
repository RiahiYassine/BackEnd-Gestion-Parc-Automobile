package au.gestionparcautomobile.aulsh.records;

import au.gestionparcautomobile.aulsh.entities.Vehicule;
import au.gestionparcautomobile.aulsh.entities.VehiculeSpecif;

public record VehiculeRequest(
        Vehicule vehicule,
        VehiculeSpecif vehiculeSpecif
) {
}
