package com.telia.Lease_management.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.Validation;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

@AllArgsConstructor
@Service
@Slf4j
public class NotificationService {

    JavaMailSender javaMailSender;
    private AppConfig appConfig;


    public void envoyer(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Votre code d'activation");

        String texte = String.format(
                "Bonjour %s, <br /> Votre code d'action est %s; A bientôt",
                validation.getUser().getFirstName(),
                validation.getCode()
                );
        message.setText(texte);

        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRequestNotificationToDgaieUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Nouvelle Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer qu'une nouvelle expression de besoin concernant la location de bâtiments a été soumise par un CPM.\n"
                + "Voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() +"\n\n"
                + "Pour garantir un suivi optimal de cette demande, nous vous prions de bien vouloir vous connecter à la plateforme de Gestion des Baux et consulter les détails associés à cette expression de besoin.\n\n"
                + "Nous vous remercions par avance pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }


    public void sendRequestNotificationToOrdonatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Demande de Complément pour Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Nous vous informons que votre expression de besoin concernant la location de bâtiments a été retournée pour complément.\n\n"
                + "Afin d'assurer un traitement optimal de votre demande, nous vous prions de bien vouloir vous connecter à la plateforme de Gestion des Baux pour consulter les détails et apporter les informations manquantes.\n\n"
                + "Nous vous remercions pour votre compréhension et votre collaboration.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }


    public void sendApprovalRequestToOrdonnatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer que votre demande d'expression de besoin concernant la location de bâtiments a été approuvée par la DAIE.\n"
                + "Voici un brève rappel de votre demande :\n"
                + rentalRequestToSend.getDescription() +"\n\n"
                + "Actuellement, votre demande est en attente de suivi par la CNOI. Nous vous tiendrons informé(e) de toute évolution concernant le traitement de votre demande.\n"
                + "Nous vous remercions pour votre coopération et restons à votre disposition pour toute question ou information complémentaire.\n\n"
                + "Cordialement,\n"
                + "L'Équipe Gestion des Baux");
                       
        javaMailSender.send(message);
        sendToME(message);
    }


    public void sendApprovalRequestToCNOIUser(Users userCnoi, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userCnoi.getEmail());
        message.setSubject("Nouvelle Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userCnoi.getFirstName() + " " + userCnoi.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer qu'une nouvelle expression de besoin concernant la location de bâtiments a été approuvée par la DAIE.\n"
                + "Voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() +"\n\n"
                + "Pour garantir un suivi optimal de cette demande, nous vous prions de bien vouloir vous connecter à la plateforme de Gestion des Baux et consulter les détails associés à cette expression de besoin.\n"
                + "Nous vous remercions par avance pour votre coopération.\n\n"
                + "Cordialement,\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRejectRequestToDgaieUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Notification de Rejet de Demande d'Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Nous regrettons de vous informer que la expression de besoin concernant la location de bâtiments a été rejetée par la CNOI.\n\n"
                + "Pour votre référence, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Nous vous invitons à vous connecter à la plateforme de Gestion des Baux pour consulter les détails et obtenir davantage d'informations.\n\n"
                + "Nous vous remercions pour votre compréhension.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
        
        javaMailSender.send(message);
        sendToME(message);
        
    }

    public void sendRejectRequestToOrdonnatorUser(Users userOrdonnator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonnator.getEmail());
        message.setSubject("Notification de Rejet de Votre Demande d'Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonnator.getFirstName() + " " + userOrdonnator.getLastName() + ",\n\n"
                + "Nous regrettons de vous informer que votre expression de besoin concernant la location de bâtiments a été rejetée par la CNOI.\n\n"
                + "Pour votre référence, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
        
        javaMailSender.send(message);
        sendToME(message);
        
    }

    public void sendHeldRequestToOrdonnatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer que votre expression de besoin concernant la location de bâtiments a été retenue par la CNOI.\n\n"
                + "Pour votre information, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Une étude sera désormais effectuée pour identifier les bâtiments disponibles correspondant à votre requête.\n"
                + "Si un bâtiment correspondant est trouvé, il fera l'objet d'une inspection. Nous vous tiendrons informé(e) de l'évolution de votre demande.\n\n"
                + "Nous vous remercions pour votre patience et votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendHeldRequestToDgaierUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Mise à Jour Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer que l'expression de besoin concernant la location de bâtiments que vous avez approuvée a été retenue par la CNOI.\n\n"
                + "Pour votre information, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Une étude sera désormais effectuée pour identifier les bâtiments disponibles correspondant à votre requête.\n"
                + "Si un bâtiment correspondant est trouvé, il fera l'objet d'une inspection. Nous vous tiendrons informé(e) des prochaines étapes.\n\n"
                + "Nous vous remercions pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRequestToCouncilMinisterToOrdonnatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer que votre expression de besoin concernant la location de bâtiments a été examinée avec succès et a atteint le dernier carré.\n"
                +" La demande a été transférée au prochain conseil des ministres pour approbation.\n\n"
                + "Pour rappel, voici une brève description de votre demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Nous espérons une issue favorable et vous tiendrons informé(e) de la décision finale dès qu'elle sera prise.\n\n"
                + "Nous vous remercions pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRequestToCouncilMinisterToDgaieUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Mise à Jour Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Nous avons le plaisir de vous informer que l'expression de besoin concernant la location de bâtiments que vous avez validée a atteint le dernier carré.\n"
                + "La demande a été transférée au prochain conseil des ministres pour approbation.\n\n"
                + "Pour rappel, voici une brève description de votre demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Nous espérons une issue favorable et vous tiendrons informé(e) de la décision finale dès qu'elle sera prise.\n\n"
                + "Nous vous remercions pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }


    public void sendValidationCouncilMinisterToDgaieUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Mise à Jour Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Bonne nouvelle !!! \n\n"
                + "Nous avons le plaisir de vous informer que l'expression de besoin concernant la location de bâtiments que vous avez validée, a été approuvée par le conseil des minitres.\n"
                + "Pour rappel, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Pour finaliser le traitement de cette demande, nous vous prions de bien vouloir vous connecter à la plateforme de Gestion des Baux et consulter les détails associés à cette expression de besoin.\n\n"
                + "Nous vous remercions pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendValidationCouncilMinisterToOrdonnatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Bonne nouvelle !!! \n\n"
                + "Nous rappel, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Votre demande est maintenant transmise à la DAIE pour finalisation.\n\n"
                + "Nous vous remercions pour votre coopération et vous tiendrons informé(e) de la suite des démarches.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");
                
        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRejectionCouncilMinisterToOrdonnatorUser(Users userOrdonator, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userOrdonator.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userOrdonator.getFirstName() + " " + userOrdonator.getLastName() + ",\n\n"
                + "Nous regrettons de vous informer que l'expression de besoin concernant votre location de bâtiments n'a pas été approuvée par le conseil des ministres.\n\n"
                + "Pour rappel, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Nous comprenons que cette décision puisse être décevante.\n\n"
                + "Nous vous remercions pour votre coopération et votre compréhension.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");

        javaMailSender.send(message);
        sendToME(message);
    }

    public void sendRejectionCouncilMinisterToDgaieUser(Users userDgaie, RentalRequest rentalRequestToSend) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo(userDgaie.getEmail());
        message.setSubject("Mise à Jour de Votre Expression de Besoin de Location de Bâtiments");

        message.setText("Cher(e) M./Mme " + userDgaie.getFirstName() + " " + userDgaie.getLastName() + ",\n\n"
                + "Nous regrettons de vous informer que l'expression de besoin concernant la location de bâtiments que vous avez validée n'a pas été approuvée par le conseil des ministres.\n\n"
                + "Pour votre information, voici une brève description de la demande :\n"
                + rentalRequestToSend.getDescription() + "\n\n"
                + "Nous vous remercions pour votre coopération.\n\n"
                + "Cordialement,\n\n"
                + "L'Équipe Gestion des Baux");

        javaMailSender.send(message);
        sendToME(message);
    }

    
    public void sendToME(SimpleMailMessage content) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.appConfig.getEmailSetFrom());
        message.setTo("wandanalikecoding@gmail.com");
        message.setSubject(content.getSubject() +" "+ content.getTo());

        message.setText(content.getText());

        javaMailSender.send(message);
    }
}
