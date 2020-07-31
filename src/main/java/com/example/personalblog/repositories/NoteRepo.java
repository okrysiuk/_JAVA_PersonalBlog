package com.example.personalblog.repositories;


import com.example.personalblog.entities.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepo extends CrudRepository<Note, Long> {

    List<Note> findByTag(String tag); // findByTag стандартный метод описаный в документации Spring JPA
}
