package au.gestionparcautomobile.aulsh.services.pdfGenerator;

import au.gestionparcautomobile.aulsh.entities.Mission;

public interface IPdfGeneratorService {

    byte[] generateMissionPdf(Mission mission);

}
