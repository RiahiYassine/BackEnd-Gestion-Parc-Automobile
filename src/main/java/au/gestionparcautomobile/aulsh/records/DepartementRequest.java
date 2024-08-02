package au.gestionparcautomobile.aulsh.records;

import au.gestionparcautomobile.aulsh.entities.Chef;
import au.gestionparcautomobile.aulsh.entities.Departement;

public record DepartementRequest(
        Departement departement,
        Chef chef
) {
}
