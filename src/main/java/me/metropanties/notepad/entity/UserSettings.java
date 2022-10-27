package me.metropanties.notepad.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_settings")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {

    @Id
    private Long id;

    @Getter(AccessLevel.NONE)
    private Boolean shouldNotify;

    public UserSettings(long id) {
        this.id = id;
        this.shouldNotify = true;
    }

    public boolean shouldNotify() {
        return this.shouldNotify;
    }

}
