package me.metropanties.notepad.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropanties.notepad.entity.UserSettings;
import me.metropanties.notepad.repository.UserSettingsRepository;
import me.metropanties.notepad.service.UserSettingsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    private final Set<UserSettings> userSettings = Collections.synchronizedSet(new HashSet<>());
    private final UserSettingsRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void load() {
        log.info("Loading user settings...");
        for (UserSettings userSettings : this.repository.findAll()) {
            if (userSettings == null) {
                continue;
            }

            this.userSettings.add(userSettings);
        }
        log.info("Loaded user settings.");
    }

    @PreDestroy
    public void shutdown() {
        log.info("Saving user settings...");
        this.repository.saveAll(this.userSettings);
        log.info("Saved user settings.");
    }

    private boolean contains(long userId) {
        return this.userSettings.stream()
                .anyMatch(userSettings -> userSettings.getId() == userId);
    }

    @Override
    public void insertSettings(@NotNull UserSettings settings) {
        if (this.contains(settings.getId())) {
            return;
        }

        this.userSettings.add(settings);
        log.info("Inserted user settings for user with id " + settings.getId());
    }

    @Override
    public boolean toggleNotify(long userId) {
        return this.userSettings.stream()
                .filter(userSettings -> userSettings.getId() == userId)
                .findFirst()
                .map(settings -> {
                    this.userSettings.remove(settings);
                    settings.setShouldNotify(!settings.shouldNotify());
                    this.userSettings.add(settings);
                    return settings.shouldNotify();
                })
                .orElse(false);
    }

    @Override
    public boolean exists(long userId) {
        return this.contains(userId);
    }

    @Override
    public Optional<UserSettings> findUserSettings(long userId) {
        if (!this.contains(userId)) {
            return this.repository.findById(userId).map(settings -> {
                this.userSettings.add(settings);
                return settings;
            });
        }

        return this.userSettings.stream()
                .filter(userSettings -> userSettings.getId() == userId)
                .findFirst();
    }

}
