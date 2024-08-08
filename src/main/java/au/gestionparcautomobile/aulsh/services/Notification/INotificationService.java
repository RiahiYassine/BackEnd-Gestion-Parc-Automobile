package au.gestionparcautomobile.aulsh.services.Notification;

import au.gestionparcautomobile.aulsh.entities.Alerte;

public interface INotificationService {

    void sendNotification(Alerte alerte);
}
