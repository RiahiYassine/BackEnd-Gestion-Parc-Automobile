package au.gestionparcautomobile.aulsh.services.Email;

public interface IEmailService {

    void sendEmail(String to, String subject, String text);

}
