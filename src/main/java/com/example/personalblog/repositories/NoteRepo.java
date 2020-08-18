package com.example.personalblog.repositories;


import com.example.personalblog.entities.Note;
import com.example.personalblog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;



public interface NoteRepo extends CrudRepository<Note, Long> {

    Page<Note> findByTag(String tag, Pageable pageable);

    Page<Note> findAll(Pageable pageable);

    Page<Note> findByAuthor(User currentUser, Pageable pageable);

}
