package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRegistrationDTO;
import com.alphasolutions.piauieventos.exception.EventNotFoundException;
import com.alphasolutions.piauieventos.exception.LocationNotFoundException;
import com.alphasolutions.piauieventos.mapper.EventLocationMapper;
import com.alphasolutions.piauieventos.mapper.EventMapper;
import com.alphasolutions.piauieventos.model.Event;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.model.Subscription;
import com.alphasolutions.piauieventos.model.SubscriptionId;
import com.alphasolutions.piauieventos.repository.EventRepository;
import com.alphasolutions.piauieventos.repository.EventLocationRepository;
import com.alphasolutions.piauieventos.repository.SubscriptionRepository;
import com.alphasolutions.piauieventos.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    @Value("${simpla.key}")
    private String symplaToken;
    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    private final EventMapper eventMapper;
    private final EventLocationService eventLocationService;
    private final EventLocationMapper eventLocationMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            EventLocationRepository eventLocationRepository,
                            EventMapper eventMapper,
                            EventLocationService eventLocationService,
                            EventLocationMapper eventLocationMapper,
                            UserRepository userRepository,
                            SubscriptionRepository subscriptionRepository, RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.eventRepository = eventRepository;
        this.eventLocationRepository = eventLocationRepository;
        this.eventMapper = eventMapper;
        this.eventLocationService = eventLocationService;
        this.eventLocationMapper = eventLocationMapper;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    @Override
    @Transactional
    public EventResponseDTO create(EventRequestDTO dto) {
        eventLocationMapper.eventLocationDtoToEventLocation(dto.getEventLocationDTO());
        EventLocation eventLocation;
        eventLocation = eventLocationService.addLocation(dto.getEventLocationDTO());
        Event event = eventMapper.toEntity(dto, eventLocation);
        event.setId(null);
        event.setVersion(null);
        event.setLocation(eventLocation);
        Event savedEvent = eventRepository.save(event);

        return eventMapper.toDTO(savedEvent);
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public Page<EventResponseDTO> listEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(eventMapper::toDTO);
    }

    @Override
    public EventResponseDTO update(Long id, EventRequestDTO dto) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        EventLocation location = eventLocationRepository.findById(dto.getEventLocationDTO().id())
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + dto.getEventLocationDTO().id()));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
        existing.setEventDate(dto.getEventDate());
        existing.setEventType(dto.getEventType());
        existing.setMaxSubs(dto.getMaxSubs());
        existing.setLocation(location);

        Event saved = eventRepository.save(existing);
        return eventMapper.toDTO(saved);
    }

    private void fillSubscribersCount(EventResponseDTO dto, Long eventId) {
        int count = (int) subscriptionRepository.countByEventId(eventId);
        dto.setSubscribersCount(count);
    }

    private void fillSubscribersCount(List<EventResponseDTO> dtos, List<Event> events) {
        for (int i = 0; i < dtos.size(); i++) {
            EventResponseDTO dto = dtos.get(i);
            Long eventId = events.get(i).getId();
            fillSubscribersCount(dto, eventId);
        }
    }

    @Override
    public EventResponseDTO findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
        EventResponseDTO dto = eventMapper.toDTO(event);
        fillSubscribersCount(dto, event.getId());
        return dto;
    }

    @Override
    @Transactional
    public void registerUser(Long eventId, UserRegistrationDTO registrationDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

        UserModel user = userRepository.findById(registrationDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + registrationDTO.userId()));

        SubscriptionId subscriptionId = new SubscriptionId(user.getId(), event.getId());

        if (subscriptionRepository.existsById(subscriptionId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already registered for this event.");
        }

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);
        subscription.setEvent(event);

        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void unregisterUser(Long eventId, Long userId) {
        SubscriptionId subscriptionId = new SubscriptionId(userId, eventId);

        if (! subscriptionRepository.existsById(subscriptionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not registered for this event.");
        }

        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public List<EventResponseDTO> upcomingEvents() {
        List<Event> events = eventRepository.findByEventDateGreaterThanEqualOrderByEventDateDesc(LocalDateTime.now(),Limit.of(5));
        List<EventResponseDTO> dtos = eventMapper.toDTO(events);
        fillSubscribersCount(dtos, events);
        return dtos;
    }

    @Override
    public List<EventResponseDTO> nextWeekEvents() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nextWeek = today.plusWeeks(1);
        List<Event> events = eventRepository.findByEventDateBetweenOrderByEventDateAsc(today, nextWeek, PageRequest.of(0, 20));
        List<EventResponseDTO> dtos = eventMapper.toDTO(events);
        fillSubscribersCount(dtos, events);
        return dtos;
    }

    @Override
    public List<EventResponseDTO> nextMonthEvents() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nextMonth = today.plusMonths(1);
        List<Event> events = eventRepository.findByEventDateBetweenOrderByEventDateAsc(today, nextMonth, PageRequest.of(0, 50));
        List<EventResponseDTO> dtos = eventMapper.toDTO(events);
        fillSubscribersCount(dtos, events);
        return dtos;
    }

    @Override
    public List<EventResponseDTO> mostSubscribedEvents() {
        List<Long> eventList = subscriptionRepository.countTopSubscriptionsByEvent(Limit.of(5));
        List<Event> events = eventRepository.findAllById(eventList);
        List<EventResponseDTO> dtos = eventMapper.toDTO(events);
        fillSubscribersCount(dtos, events);
        return dtos;
    }

    @Override
    public void feedEvent() {
        String dateRange = "2025-01-01,2025-12-31";

        String symplaUrl = "https://api.sympla.com.br/public/v4/events"
                + "?state=PI"
                + "&range=" + dateRange
                + "&page=1"
                + "&limit=100";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("s_token", symplaToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    symplaUrl,
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );

            System.out.println("=== Sympla API Response (Eventos do Piauí) ===");
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body:");
            System.out.println(response.getBody());
            System.out.println("==============================================");

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode dataNode = rootNode.get("data");

                if (dataNode != null && dataNode.isArray()) {
                    System.out.println("\n=== Detalhes dos Eventos do Piauí ===");
                    int eventCount = 0;

                    for (JsonNode eventNode : dataNode) {
                        eventCount++;
                        System.out.println("\nEvento #" + eventCount + ":");

                        if (eventNode.has("id")) {
                            System.out.println("  ID: " + eventNode.get("id").asText());
                        }
                        if (eventNode.has("name")) {
                            System.out.println("  Nome: " + eventNode.get("name").asText());
                        }
                        if (eventNode.has("start_date")) {
                            System.out.println("  Data Início: " + eventNode.get("start_date").asText());
                        }
                        if (eventNode.has("end_date")) {
                            System.out.println("  Data Fim: " + eventNode.get("end_date").asText());
                        }

                        JsonNode addressNode = eventNode.get("address");
                        if (addressNode != null) {
                            if (addressNode.has("city")) {
                                System.out.println("  Cidade: " + addressNode.get("city").asText());
                            }
                            if (addressNode.has("state")) {
                                System.out.println("  Estado: " + addressNode.get("state").asText());
                            }
                        }

                        if (eventNode.has("url")) {
                            System.out.println("  URL: " + eventNode.get("url").asText());
                        }
                    }

                    if (rootNode.has("total")) {
                        System.out.println("\n--- Informações de Paginação ---");
                        System.out.println("Total de eventos: " + rootNode.get("total").asInt());
                        System.out.println("Página atual: " + rootNode.get("page").asInt());
                        System.out.println("Total de páginas: " + rootNode.get("total_page").asInt());
                        System.out.println("Eventos por página: " + rootNode.get("limit").asInt());
                    }

                    System.out.println("\n======================================");
                }
            }

        } catch (Exception e) {
            System.err.println("Error calling Sympla API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
