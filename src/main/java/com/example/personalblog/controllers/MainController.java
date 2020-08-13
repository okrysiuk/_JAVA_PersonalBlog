package com.example.personalblog.controllers;

import com.example.personalblog.entities.Note;
import com.example.personalblog.entities.User;
import com.example.personalblog.repositories.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private NoteRepo noteRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String landing(){
        return "landing";
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false, defaultValue = "") String filter,
                       @AuthenticationPrincipal User user, Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Note> page;

        if(filter != null && !filter.isEmpty()) {
            page = noteRepo.findByTag(filter, pageable);
        } else {
            page = noteRepo.findAll(pageable);
        }

        model.addAttribute("notes", page);
        model.addAttribute("filter", page);
        model.addAttribute("url", "/home");
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/user-notes/{user}")
    public String userNotes(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user, Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Note> page;

        page = noteRepo.findByAuthor(currentUser, pageable);
        model.addAttribute("notes", page);
        model.addAttribute("url", user.getId());

        return "user-notes";
    }

    @GetMapping("/note-add")
    public String noteAdd(Model model) {

        return "note-add";
    }

    @PostMapping("/addition")
    public String noteAddition(@AuthenticationPrincipal User user,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String text,
                               @RequestParam(required = false) String tag, Model model) throws IOException {
        Note note = new Note(user, title, text, tag);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            note.setFilename(resultFilename);
        }

        noteRepo.save(note);
        return "redirect:/home";
    }

    @GetMapping("/note/{id}")
    public String noteDetails(@PathVariable(value = "id")long noteId, @AuthenticationPrincipal User user, Model model) {
        return getString(noteId, user, model);
    }

    @GetMapping("/user-notes/note/{id}")
    public String userNoteDetails(@PathVariable(value = "id")long noteId, @AuthenticationPrincipal User user, Model model) {
        return getString(noteId, user, model);
    }

    private String getString(@PathVariable("id") long noteId, @AuthenticationPrincipal User user, Model model) {
        if(!noteRepo.existsById(noteId)){
            return "redirect:/";
        }
        Optional<Note> note = noteRepo.findById(noteId);
        ArrayList<Note> res = new ArrayList<>();
        note.ifPresent(res::add);
        model.addAttribute("user", user);
        model.addAttribute("detail",res);
        return "note-details";
    }

    @GetMapping("/note/{id}/edit")
    public String noteEdit(@PathVariable(value = "id")long noteId, Model model) {
        if(!noteRepo.existsById(noteId)){
            return "redirect:/home";
        }

        Optional<Note> note = noteRepo.findById(noteId);
        ArrayList<Note> res = new ArrayList<>();
        note.ifPresent(res::add);
        model.addAttribute("detail",res);
        return "note-edit";
    }

    @PostMapping("/note/{id}/edit")
    public String noteUpdating(@PathVariable(value = "id")long noteId,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String text,
                               @RequestParam(required = false) String tag, Model model) {
        Note note = noteRepo.findById(noteId).orElseThrow(() ->
                new IllegalArgumentException("Unsupported value: " + noteId) // just return it
        );
        note.setTitle(title);
        note.setText(text);
        note.setTag(tag);

        noteRepo.save(note);

        return "redirect:/home";
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
