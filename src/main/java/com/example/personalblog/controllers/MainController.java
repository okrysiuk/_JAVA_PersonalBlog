package com.example.personalblog.controllers;

import com.example.personalblog.entities.Note;
import com.example.personalblog.repositories.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private NoteRepo noteRepo;

    @GetMapping("/")
    public String landing(){
        return "landing";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Iterable<Note> notes = noteRepo.findAll();
        model.addAttribute("notes", notes);
        return "home";
    }
    @GetMapping("/note/{id}")
    public String noteDetails(@PathVariable(value = "id")long noteId, Model model) {
        if(!noteRepo.existsById(noteId)){
            return "redirect:/";
        }

        Optional<Note> note = noteRepo.findById(noteId);
        ArrayList<Note> res = new ArrayList<>();
        note.ifPresent(res::add);
        model.addAttribute("detail",res);
        return "note-details";
    }
    @PostMapping("/note/{id}/remove")
    public String removing(@PathVariable(value = "id")long noteId, Model model) {
        Note note = noteRepo.findById(noteId).orElseThrow(() ->
                new IllegalArgumentException("Unsupported value: " + noteId) // just return it
        );
        noteRepo.delete(note);

        return "redirect:/home";
    }
}
