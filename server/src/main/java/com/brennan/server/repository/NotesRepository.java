package com.brennan.server.repository;

import com.brennan.server.domain.Notes;
import com.brennan.server.dto.NotesInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<NotesInfo> findAllByOrderByCreatedAtDesc();

    Optional<NotesInfo> findNotesById(Long id);

    List<NotesInfo> findByTitleContainingIgnoreCase(String title);
}
