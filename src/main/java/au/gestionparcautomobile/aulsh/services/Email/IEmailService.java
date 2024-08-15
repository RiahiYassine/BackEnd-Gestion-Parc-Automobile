package au.gestionparcautomobile.aulsh.services.Email;

import org.thymeleaf.context.Context;

public interface IEmailService {

    void sendEmail(String to, String subject, String text);
    public void sendEmaill(String to, String subject, Context context);
}
