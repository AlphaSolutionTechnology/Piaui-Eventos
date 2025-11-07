package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.EventUpdateDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
public class EventServiceImpl implements EventService {

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
                            SubscriptionRepository subscriptionRepository) {
        this.eventRepository = eventRepository;
        this.eventLocationRepository = eventLocationRepository;
        this.eventMapper = eventMapper;
        this.eventLocationService = eventLocationService;
        this.eventLocationMapper = eventLocationMapper;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
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
        event.setCreatedBy(dto.getCreatedBy());
        Event savedEvent = eventRepository.save(event);

        EventResponseDTO response = eventMapper.toDTO(savedEvent);
        response.setSubscribedCount(0); // Novo evento não tem inscritos
        return response;
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
        return events.map(event -> {
            EventResponseDTO dto = eventMapper.toDTO(event);
            Long subscribedCount = subscriptionRepository.countByEventId(event.getId());
            dto.setSubscribedCount(subscribedCount.intValue());
            return dto;
        });
    }

    @Override
    public EventResponseDTO update(Long id, EventRequestDTO dto) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        // Validação de segurança: apenas o criador pode editar
        if (dto.getCreatedBy() != null && !existing.getCreatedBy().equals(dto.getCreatedBy())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this event");
        }

        // Atualizar location se fornecido
        if (dto.getEventLocationDTO() != null && dto.getEventLocationDTO().id() != null) {
            EventLocation location = eventLocationRepository.findById(dto.getEventLocationDTO().id())
                    .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + dto.getEventLocationDTO().id()));
            existing.setLocation(location);
        }

        // Atualização parcial - apenas campos não nulos são atualizados
        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }
        if (dto.getImageUrl() != null) {
            existing.setImageUrl(dto.getImageUrl());
        }
        if (dto.getEventDate() != null) {
            existing.setEventDate(dto.getEventDate());
        }
        if (dto.getEventType() != null && !dto.getEventType().isBlank()) {
            existing.setEventType(dto.getEventType());
        }
        if (dto.getMaxSubs() != null) {
            existing.setMaxSubs(dto.getMaxSubs());
        }

        Event saved = eventRepository.save(existing);
        EventResponseDTO response = eventMapper.toDTO(saved);
        Long subscribedCount = subscriptionRepository.countByEventId(id);
        response.setSubscribedCount(subscribedCount.intValue());
        return response;
    }

    @Override
    public EventResponseDTO updateEventSecure(Long id, EventUpdateDTO dto) {
        // Obter usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Buscar evento
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        // Validação de segurança: apenas o criador pode editar
        if (!existing.getCreatedBy().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this event");
        }

        // Atualizar location se fornecido
        if (dto.getEventLocationDTO() != null && dto.getEventLocationDTO().id() != null) {
            EventLocation location = eventLocationRepository.findById(dto.getEventLocationDTO().id())
                    .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + dto.getEventLocationDTO().id()));
            existing.setLocation(location);
        }

        // Atualização parcial - apenas campos não nulos são atualizados
        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }
        if (dto.getImageUrl() != null) {
            existing.setImageUrl(dto.getImageUrl());
        }
        if (dto.getEventDate() != null) {
            existing.setEventDate(dto.getEventDate());
        }
        if (dto.getEventType() != null && !dto.getEventType().isBlank()) {
            existing.setEventType(dto.getEventType());
        }
        if (dto.getMaxSubs() != null) {
            existing.setMaxSubs(dto.getMaxSubs());
        }

        Event saved = eventRepository.save(existing);
        EventResponseDTO response = eventMapper.toDTO(saved);
        Long subscribedCount = subscriptionRepository.countByEventId(id);
        response.setSubscribedCount(subscribedCount.intValue());
        return response;
    }

    @Override
    public EventResponseDTO findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        EventResponseDTO dto = eventMapper.toDTO(event);
        Long subscribedCount = subscriptionRepository.countByEventId(id);
        dto.setSubscribedCount(subscribedCount.intValue());

        return dto;
    }

    @Override
    public Page<EventResponseDTO> findByUserId(Long userId, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByCreatedBy(userId, pageable);
        return events.map(event -> {
            EventResponseDTO dto = eventMapper.toDTO(event);
            Long subscribedCount = subscriptionRepository.countByEventId(event.getId());
            dto.setSubscribedCount(subscribedCount.intValue());
            return dto;
        });
    }

    @Override
    @Transactional
    public void registerUser(Long eventId, UserRegistrationDTO registrationDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

        UserModel user = userRepository.findById(registrationDTO.userId().longValue())
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
    public Page<EventResponseDTO> findSubscribedEventsByUserId(Long userId, Pageable pageable) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        List<Event> subscribedEvents = subscriptions.stream()
                .map(Subscription::getEvent)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), subscribedEvents.size());

        List<EventResponseDTO> eventDTOs = subscribedEvents.subList(start, end).stream()
                .map(event -> {
                    EventResponseDTO dto = eventMapper.toDTO(event);
                    Long subscribedCount = subscriptionRepository.countByEventId(event.getId());
                    dto.setSubscribedCount(subscribedCount.intValue());
                    return dto;
                })
                .toList();

        return new org.springframework.data.domain.PageImpl<>(eventDTOs, pageable, subscribedEvents.size());
    }
}