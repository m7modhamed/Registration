package com.stickynotes.notes;

import java.util.Optional;

public interface InoteService {
    void saveNote(Note note);

    void updateNote(long id);

    Optional<Note> FindNote(long id);

    void deleteNote(long id);

}
