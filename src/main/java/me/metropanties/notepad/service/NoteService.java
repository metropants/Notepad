package me.metropanties.notepad.service;

import me.metropanties.notepad.dto.NoteDto;
import me.metropanties.notepad.entity.Note;
import me.metropanties.notepad.exception.NoteNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface NoteService {

    void createNote(@NotNull NoteDto dto);

    void deleteNote(long id) throws NoteNotFoundException;

    boolean isNoteCreator(long noteId, long userId);

    Optional<Note> findNoteById(long id);

    LinkedList<Note> findAllNotesByUserId(long userId);

    List<Note> findAllNotes();

}
