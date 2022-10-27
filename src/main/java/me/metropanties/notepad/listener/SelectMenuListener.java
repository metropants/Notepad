package me.metropanties.notepad.listener;

import lombok.RequiredArgsConstructor;
import me.metropanties.springdiscordstarter.listener.annotation.Listener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

@Listener
@RequiredArgsConstructor
public class SelectMenuListener {

    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
            Member member = event.getMember();
            String[] componentId = event.getComponentId().split(":");
            if (member == null || !componentId[0].equals(member.getId())) {
                return;
            }

            event.getMessage().delete().queue();
            TextInput input = TextInput.create("input", "Note", TextInputStyle.PARAGRAPH)
                    .setRequiredRange(6, 255)
                    .build();

            String modalId = "note:%s".formatted(member.getId());
            Modal modal = Modal.create(modalId, "Create a new note")
                    .addActionRow(input)
                    .build();
            event.replyModal(modal).queue();
    }

}
