package com.stickynotes.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService implements InoteService{

    private final NoteRepository repo;
    @Override
    public void saveNote(Note note) {
        repo.save(note);
    }

    @Override
    public void updateNote(long id) {
        Optional<Note> note=repo.findById(id);
        if(note.isPresent()){
            repo.save(note.get());
        }
    }

    @Override
    public Optional<Note> FindNote(long id) {
        Optional<Note> note=repo.findById(id);

        return note;
    }

    @Override
    public void deleteNote(long id) {
        repo.deleteById(id);
    }


}
