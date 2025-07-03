package com.brennan.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import  org.hibernate.annotations.ColumnDefault;
import java.time.Instant;

@Entity
@Table(name = "notes")
public class Notes {
    // --- Initializations ---
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "note_id_gen")
    @SequenceGenerator(name = "note_id_gen", sequenceName = "note_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 200)
    @NotNull
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 500)
    @NotNull
    @Column(name = "body", nullable = false, length = 500)
    private String body;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // --- Constructors ---
    public Notes() {
    }

    // A convenience constructor for creating new bookmarks (without ID and timestamps initially)
    public Notes(String title, String body) {
        this.title = title;
        this.body = body;
    }

    // --- Lifecycle Callbacks for Timestamps ---
    @PrePersist // Called before the entity is first persisted (saved)
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate // Called before the entity is updated
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // --- Getters and Setters ---
    // Usually, no need to provide a setter for auto-generated IDs
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
