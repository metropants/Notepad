package me.metropanties.notepad.command;

import lombok.RequiredArgsConstructor;
import me.metropanties.notepad.dto.NoteDto;
import me.metropanties.notepad.entity.Note;
import me.metropanties.notepad.exception.NoteNotFoundException;
import me.metropanties.notepad.service.NoteService;
import me.metropanties.notepad.service.UserSettingsService;
import me.metropanties.springdiscordstarter.command.annotation.Command;
import me.metropanties.springdiscordstarter.command.annotation.CommandExecutor;
import me.metropanties.springdiscordstarter.command.annotation.Option;
import me.metropanties.springdiscordstarter.command.annotation.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command(
        name = "notes",
        description = "All notes related commands.",
        subCommands = {
                @SubCommand(name = "list", description = "List all notes."),
                @SubCommand(name = "create", description = "Create a new note."),
                @SubCommand(name = "notify", description = "Toggles notification for not completed notes."),
                @SubCommand(name = "delete", description = "Delete a note.", options = {
                        @Option(type = OptionType.INTEGER, name = "id", description = "The id of the note to delete.", required = true)
                }),
                @SubCommand(name = "complete", description = "Toggles between the complete state of the note.", options = {
                        @Option(type = OptionType.INTEGER, name = "id", description = "The id of the note to mark as complete.", required = true)
                }),
        }
)
@RequiredArgsConstructor
public class NoteCommand {

    private final NoteService noteService;
    private final UserSettingsService userSettingsService;
    private final EmbedBuilder eb;

    @CommandExecutor
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String subCommand = event.getSubcommandName();
        if (subCommand == null) {
            return;
        }

        switch (subCommand) {
            case "create" -> createTodo(event);
            case "notify" -> toggleNotification(event);
            case "complete" -> completeTodo(event);
            case "delete" -> deleteTodo(event);
            case "list" -> listTodos(event);
        }
    }

    private void createTodo(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        TextInput input = TextInput.create("input", "Note", TextInputStyle.PARAGRAPH)
                .setRequiredRange(6, 255)
                .build();

        String modalId = "note:%s".formatted(member.getId());
        Modal modal = Modal.create(modalId, "Create a new note")
                .addActionRow(input)
                .build();
        event.replyModal(modal).queue();
    }

    private void toggleNotification(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        long userId = member.getIdLong();
        boolean toggled = this.userSettingsService.toggleNotify(userId);
        if (toggled) {
            event.replyEmbeds(this.eb.setDescription("Notifications are now enabled.").build())
                    .setEphemeral(true)
                    .queue();
        } else {
            event.replyEmbeds(this.eb.setDescription("Notifications are now disabled.").build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    private void completeTodo(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (event.getOption("id") == null) {
            return;
        }

        long noteId = event.getOption("id").getAsLong();
        if (!this.noteService.isNoteCreator(noteId, member.getIdLong())) {
            event.replyEmbeds(eb.setDescription("Note with id %s not found.".formatted(noteId)).build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        this.noteService.findNoteById(noteId).ifPresentOrElse(note -> {
            NoteDto noteDto = new NoteDto(note.getId(), member.getIdLong(), note.getText(), !note.getCompleted());
            this.noteService.createNote(noteDto);
            event.replyEmbeds(eb.setDescription("Note successfully updated.").build())
                    .setEphemeral(true)
                    .queue();
        }, () -> event.replyEmbeds(eb.setDescription("Note with id %s not found.".formatted(noteId)).build())
                .setEphemeral(true)
                .queue());
    }

    private void deleteTodo(@NotNull SlashCommandInteractionEvent event) {
        if (event.getOption("id") == null) {
            return;
        }

        long noteId = event.getOption("id").getAsLong();
        if (!this.noteService.isNoteCreator(noteId, event.getMember().getIdLong())) {
            event.replyEmbeds(eb.setDescription("Note with id %s not found.".formatted(noteId)).build())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        try {
            this.noteService.deleteNote(noteId);
            event.replyEmbeds(this.eb.setDescription("Note successfully deleted.").build())
                    .setEphemeral(true)
                    .queue();
        } catch (NoteNotFoundException e) {
            event.replyEmbeds(this.eb.setDescription(e.getLocalizedMessage()).build())
                    .setEphemeral(true)
                    .queue();
        }
    }

    private void listTodos(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        List<Note> memberNotes = this.noteService.findAllNotesByUserId(member.getIdLong());
        StringBuilder sb = new StringBuilder();
        if (memberNotes.isEmpty()) {
            sb.append("You currently don't have any notes. Use `/notes create` to create one.");
        } else {
            for (Note note : memberNotes) {
                sb.append(note.getId())
                        .append(" • ")
                        .append(note.getText())
                        .append(" -  ")
                        .append(note.getCompleted() ? "**Completed**" : "**Not completed**")
                        .append("\n");
            }
        }

        String notes = sb.toString();
        event.replyEmbeds(this.eb.setDescription(notes).build())
                .setEphemeral(true)
                .queue();
    }

}
