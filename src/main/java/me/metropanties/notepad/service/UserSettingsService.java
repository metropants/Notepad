package me.metropanties.notepad.service;

import me.metropanties.notepad.entity.UserSettings;

import java.util.Optional;

public interface UserSettingsService {

    void insertSettings(UserSettings settings);

    boolean toggleNotify(long userId);

    boolean exists(long userId);

    Optional<UserSettings> findUserSettings(long userId);

}
