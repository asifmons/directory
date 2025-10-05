package com.stjude.directory.service;

import com.stjude.directory.model.AnniversaryEvent;
import com.stjude.directory.model.BirthdayEvent;
import com.stjude.directory.repository.EventRepository;
import com.stjude.directory.repository.FamilyRepository;
import com.stjude.directory.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventSchedulerService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void computeAndStoreEvents() {
        eventRepository.deleteAll();

        List<BirthdayEvent> birthdayEvents = memberRepository.findUpcomingBirthdays();
        eventRepository.saveAll(birthdayEvents);

        List<AnniversaryEvent> anniversaryEvents = familyRepository.findUpcomingAnniversaries();
        List<AnniversaryEvent> anniversaryEventsToSave = anniversaryEvents.stream().map(event -> {
            String coupleName = event.getCouple().stream().map(c -> c.getName()).collect(Collectors.joining(" & "));
            return new AnniversaryEvent(coupleName, event.getDate(), event.getFamilyId(), event.getCouple());
        }).collect(Collectors.toList());

        eventRepository.saveAll(anniversaryEventsToSave);
    }
}
