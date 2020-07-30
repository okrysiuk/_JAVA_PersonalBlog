package com.example.personalblog.repositories;


import com.example.personalblog.entities.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepo extends CrudRepository<Note, Long> {
}
