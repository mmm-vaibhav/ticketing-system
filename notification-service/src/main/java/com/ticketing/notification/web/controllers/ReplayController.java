package com.ticketing.notification.web.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.notification.service.UserEventReplayService;
import com.ticketing.notification.utils.AppConstants;

@RestController
@RequestMapping(AppConstants.BASE_URL + AppConstants.END_POINT_URL)
public class ReplayController {
	
	private final UserEventReplayService replayService;
    private final ObjectMapper objectMapper;

    public ReplayController(UserEventReplayService replayService, ObjectMapper objectMapper) {
        this.replayService = replayService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public String replayEvent(@RequestBody String message) {
        try {
            BaseEvent event = objectMapper.readValue(message, BaseEvent.class);

            replayService.replay(event);

            return "Replay triggered for eventId: " + event.getEventId();

        } catch (Exception e) {
            return "Replay failed";
        }
    }

}
