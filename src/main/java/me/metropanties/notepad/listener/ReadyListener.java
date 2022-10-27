package me.metropanties.notepad.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropanties.springdiscordstarter.listener.annotation.Listener;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.jetbrains.annotations.NotNull;

@Listener
@Slf4j
@RequiredArgsConstructor
public class ReadyListener {

    public void onReady(@NotNull ReadyEvent event) {
        final String tag = event.getJDA().getSelfUser().getAsTag();
        log.info("{}, is now online.", tag);
    }

}
