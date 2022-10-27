package me.metropanties.notepad.listener;

import lombok.RequiredArgsConstructor;
import me.metropanties.notepad.dto.NoteDto;
import me.metropanties.notepad.service.NoteService;
import me.metropanties.springdiscordstarter.listener.annotation.Listener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class ModalListener {

    private final NoteService noteService;
    private final EmbedBuilder eb;

    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        String[] splitId = event.getModalId().split(":");
        if (!splitId[1].equals(member.getId()) || event.getValue("input") == null) {
            return;
        }

        String note = event.getValue("input").getAsString();
        this.noteService.createNote(new NoteDto(member.getIdLong(), note));
        event.replyEmbeds(this.eb.setDescription("Note successfully created!").build())
                .setEphemeral(true)
                .queue();
    }

}
