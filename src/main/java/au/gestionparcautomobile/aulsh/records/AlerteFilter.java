package au.gestionparcautomobile.aulsh.records;

import java.time.LocalDate;

public record AlerteFilter(

        String typeAlerte,
        String matricule,
        String severity

) {
}
