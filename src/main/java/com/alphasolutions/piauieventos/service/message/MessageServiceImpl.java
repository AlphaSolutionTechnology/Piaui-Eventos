package com.alphasolutions.piauieventos.service.message;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.MessageDTO;
import com.alphasolutions.piauieventos.exception.WhatsappCommunicationException;
import com.alphasolutions.piauieventos.service.event.EventService;
import com.alphasolutions.piauieventos.util.MessageUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;

@Service
public class MessageServiceImpl  implements  MessageService{

    private final EventService eventService;
    private final MessageUtil messageUtil;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    @Value("${apikey}")
    public String apiKey;

    public MessageServiceImpl(EventService eventService, MessageUtil messageUtil, RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.eventService = eventService;
        this.messageUtil = messageUtil;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void processMessageUpsert(JsonNode payload) {
        System.out.println("Payload recebido: " + payload.toString());
        String URI = "http://localhost:8089/message/sendText/piev";
        HttpHeaders headers = httpHeaders;
        headers.set("apikey", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setDelay(0);
        messageDTO.setLinkPreview(true);
        boolean isFromMe = payload.path("data").path("key").path("fromMe").asBoolean();
        String messageContent = payload.path("data").path("message").path("conversation").asText().toLowerCase(Locale.ROOT);
        String number = payload.path("data").path("key").path("remoteJid").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        List<EventResponseDTO> events = null;
        try {
            if (messageContent.contains("próximos eventos") || messageContent.contains("proximos eventos")) {
                events = eventService.upcomingEvents();
            } else if (messageContent.contains("eventos da próxima semana") || messageContent.contains("eventos da proxima semana")) {
                events = eventService.nextWeekEvents();
            } else if (messageContent.contains("eventos do próximo mês") || messageContent.contains("eventos do proximo mes")) {
                events = eventService.nextMonthEvents();
            }else if (messageContent.contains("eventos mais populares")) {
                events = eventService.mostSubscribedEvents();
            }
            if (!isFromMe || isFromMe) {
                if (events != null && !events.isEmpty()) {
                   for(EventResponseDTO event : events){
                          String formattedMessage = messageUtil.messageTemplate(event);
                          messageDTO.setNumber(number);
                          messageDTO.setText(formattedMessage);
                          String jsonPayload = objectMapper.writeValueAsString(messageDTO);
                          HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
                          restTemplate.postForEntity(URI, requestEntity, String.class);
                   }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new WhatsappCommunicationException("Erro ao enviar mensagem via WhatsApp", e);
        }
    }
}
