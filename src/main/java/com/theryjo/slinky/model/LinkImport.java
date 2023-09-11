package com.theryjo.slinky.model;

import com.theryjo.slinky.enums.ImportType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "link_import")
public class LinkImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_import_id")
    private int id;

    @Enumerated(EnumType.STRING)
    private ImportType type;

    private String name;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(columnDefinition = "TINYINT")
    private boolean completed;

    private Integer count;

    public LinkImport() {
    }

    public LinkImport(ImportType type, String name, LocalDateTime startedAt, LocalDateTime finishedAt, boolean completed, Integer count) {
        this.type = type;
        this.name = name;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.completed = completed;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImportType getType() {
        return type;
    }

    public void setType(ImportType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

