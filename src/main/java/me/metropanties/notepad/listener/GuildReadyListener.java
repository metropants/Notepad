package me.metropanties.notepad.listener;

import lombok.RequiredArgsConstructor;
import me.metropanties.notepad.entity.Note;
import me.metropanties.notepad.entity.UserSettings;
import me.metropanties.notepad.service.NoteService;
import me.metropanties.notepad.service.UserSettingsService;
import me.metropanties.springdiscordstarter.listener.annotation.Listener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Listener
@RequiredArgsConstructor
public class GuildReadyListener {

    private final NoteService noteService;
    private final UserSettingsService userSettingsService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final EmbedBuilder eb;

    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();

        guild.loadMembers(member -> {
            User user = member.getUser();
            if (!this.userSettingsService.exists(member.getIdLong())) {
                if (user.isBot()) {
                    return;
                }

                UserSettings settings = new UserSettings(member.getIdLong());
                this.userSettingsService.insertSettings(settings);
            }

            this.userSettingsService.findUserSettings(member.getIdLong()).ifPresent(settings -> {
                if (settings.shouldNotify()) {
                    LinkedList<Note> memberNotes = this.noteService.findAllNotesByUserId(member.getIdLong());
                    remind(user, memberNotes);
                }
            });
        });
    }

    private void remind(@NotNull User user, @NotNull List<Note> notes) {
        this.taskScheduler.scheduleWithFixedDelay(() -> {
            if (notes.isEmpty()) {
                return;
            }

            this.eb.clearFields();
            for (Note note : notes) {
                if (note.getCompleted()) {
                    continue;
                }

                this.eb.addField("Note #" + note.getId(), note.getText(), false);
            }

            this.eb.setTitle("Reminder");
            this.eb.setDescription("You have a note that is not completed yet.");
            user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(this.eb.build()).queue());
        }, Duration.ofDays(1));
    }

}
