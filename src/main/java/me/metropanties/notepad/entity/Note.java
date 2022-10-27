package me.metropanties.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String text;

    private Boolean completed;

    public Note(long userid, String text) {
        this.userId = userid;
        this.text = text;
        this.completed = false;
    }

}
