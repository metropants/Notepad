package me.metropanties.notepad.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String message) {
        super(message);
    }

}
