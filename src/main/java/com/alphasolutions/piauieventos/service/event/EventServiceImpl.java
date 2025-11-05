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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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
}