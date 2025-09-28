package com.alphasolutions.piauieventos.util;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class MessageUtil {
    public String messageTemplate(EventResponseDTO event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", new Locale("pt", "BR"));
        String formattedDate = event.getEventDate().format(formatter);
        EventLocationDTO eventLocation = event.getEventLocation();
        String mapLink = gerarLinkDirecoes(
                Double.parseDouble(eventLocation.latitude()),
                Double.parseDouble(eventLocation.longitude())
        );
        int inscritos = event.getSubscribersCount() != null ? event.getSubscribersCount() : 0;

        return String.format(
                """
                🎉 *%s* 🎉
                
                📅 *Data e Hora*: %s
                🏷️ *Tipo de Evento*: %s
                👥 *Inscritos*: %d pessoa%s
                📍 *Local*: %s, %s (CEP: %s)
                🗺️ [Ver no Mapa](%s)
                
                📝 *Descrição*:
                %s
                
                💡 *Participe*! Inscreva-se agora na nossa plataforma e não perca esse evento incrível! 😊
                """,
                event.getName(),
                formattedDate,
                event.getEventType(),
                inscritos,
                inscritos == 1 ? "" : "s",
                eventLocation.placeName(),
                eventLocation.fullAddress(),
                eventLocation.zipCode(),
                mapLink,
                event.getDescription()
        );
    }

    private static String gerarLinkDirecoes(double latitude, double longitude) {
        return String.format(
                "https://www.google.com/maps/dir/?api=1&destination=%f,%f",
                latitude, longitude
        );
    }
}