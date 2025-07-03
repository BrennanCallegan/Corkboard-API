package com.brennan.server.controller;

import com.brennan.server.domain.Notes;
import com.brennan.server.dto.NotesInfo;
import com.brennan.server.repository.NotesRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesRepository notesRepository;

    NotesController(NotesRepository notesRepository){
        this.notesRepository = notesRepository;
    }

    //--- CRUD Operations ---
    record CreateNotesPayload(
            @NotEmpty(message = "Title is Required")
            String title,
            @NotEmpty(message = "Body is Required")
            String body

    ){}

    @PostMapping
    ResponseEntity<Void> createNote(@Valid @RequestBody NotesController.CreateNotesPayload payload){
        var note = new Notes();
        note.setTitle(payload.title);
        note.setBody(payload.body());
        note.setCreatedAt(Instant.now());
        var savedNote = notesRepository.save(note);
        var body = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedNote.getId());
        return ResponseEntity.created(body).build();
    }

    @GetMapping
    List<NotesInfo> getNotes() {
        return notesRepository.findAllByOrderByCreatedAtDesc();
    }
    @GetMapping("/{id}")
    ResponseEntity<NotesInfo> getNoteById(@PathVariable Long id) {
        var note =
                notesRepository.findNotesById(id)
                        .orElseThrow(()-> new NotesNotFoundException("Note not found"));
        return ResponseEntity.ok(note);
    }

    record UpdateNotePayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Body is required")
            String body) {
    }
    @PutMapping("/{id}")
    ResponseEntity<Void> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NotesController.UpdateNotePayload payload) {
        var note =
                notesRepository.findById(id)
                        .orElseThrow(()-> new NotesNotFoundException("Note not found"));
        note.setTitle(payload.title());
        note.setBody(payload.body());
        note.setUpdatedAt(Instant.now());
        notesRepository.save(note);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    void deleteNote(@PathVariable Long id) {
        var note =
                notesRepository.findById(id)
                        .orElseThrow(()-> new NotesNotFoundException("Note not found"));
        notesRepository.delete(note);
    }

    @ExceptionHandler(NotesNotFoundException.class)
    ResponseEntity<Void> handle(NotesNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
