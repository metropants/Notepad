package me.metropanties.notepad.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.metropanties.notepad.dto.NoteDto;
import me.metropanties.notepad.entity.Note;
import me.metropanties.notepad.exception.NoteNotFoundException;
import me.metropanties.notepad.mapper.NoteMapper;
import me.metropanties.notepad.repository.NoteRepository;
import me.metropanties.notepad.service.NoteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;

    @Override
    public void createNote(@NotNull NoteDto dto) {
        Note note = NoteMapper.INSTANCE.toNote(dto);
        if (note == null) {
            log.warn("Mapped note from dto is null.");
            return;
        }

        this.repository.save(note);
    }

    @Override
    public void deleteNote(long id) throws NoteNotFoundException {
        if (!this.repository.existsById(id)) {
            throw new NoteNotFoundException("Note with id " + id + " does not exist.");
        }

        this.repository.deleteById(id);
    }

    @Override
    public boolean isNoteCreator(long noteId, long userId) {
        return this.repository.findById(noteId)
                .map(note -> note.getUserId() == userId)
                .orElse(false);
    }

    @Override
    public Optional<Note> findNoteById(long id) {
        return this.repository.findById(id);
    }

    @Override
    public LinkedList<Note> findAllNotesByUserId(long userId) {
        return this.repository.findAllByUserId(userId);
    }

    @Override
    public List<Note> findAllNotes() {
        return this.repository.findAll();
    }

}
