package com.stjude.directory.controller;

import com.stjude.directory.model.Event;
import com.stjude.directory.model.Comment;
import com.stjude.directory.model.Event;
import com.stjude.directory.model.Reaction;
import com.stjude.directory.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @PostMapping("/{eventId}/comments")
    public Event addComment(@PathVariable String eventId, @RequestBody Comment comment) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        event.getComments().add(comment);
        return eventRepository.save(event);
    }

    @PostMapping("/{eventId}/reactions")
    public Event addReaction(@PathVariable String eventId, @RequestBody Reaction reaction) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        event.getReactions().add(reaction);
        return eventRepository.save(event);
    }
}
