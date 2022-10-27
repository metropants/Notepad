package me.metropanties.notepad.mapper;

import me.metropanties.notepad.dto.NoteDto;
import me.metropanties.notepad.entity.Note;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoteMapper {

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    Note toNote(@NotNull NoteDto dto);

}
