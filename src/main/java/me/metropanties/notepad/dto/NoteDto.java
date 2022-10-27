package me.metropanties.notepad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private Long id;
    private Long userId;
    private String text;
    private Boolean completed;

    public NoteDto(long userId, String text) {
        this.userId = userId;
        this.text = text;
        this.completed = false;
    }

}
