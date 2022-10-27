package me.metropanties.notepad.repository;

import me.metropanties.notepad.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface NoteRepository extends JpaRepository<Note, Long> {

    LinkedList<Note> findAllByUserId(long userId);

}
