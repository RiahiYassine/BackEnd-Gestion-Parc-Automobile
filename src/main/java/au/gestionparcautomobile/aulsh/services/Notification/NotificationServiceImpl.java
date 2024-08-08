package au.gestionparcautomobile.aulsh.services.Notification;

import au.gestionparcautomobile.aulsh.entities.Alerte;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService{


    private final SimpMessagingTemplate template;

    public void sendNotification(Alerte alerte) {
        String message = "Reminder: " + alerte.getMessage();
        template.convertAndSend("/topic/alerts", message);
    }

}
