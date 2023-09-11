package com.theryjo.slinky.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theryjo.slinky.enums.LinkType;

import javax.persistence.*;

@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "link_import_id")
    @JsonIgnore
    private LinkImport linkImport;

    @Enumerated(EnumType.STRING)
    private LinkType type;

    private String value;

    public Link() {
    }

    public Link(LinkImport linkImport, LinkType type, String value) {
        this.linkImport = linkImport;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkImport getLinkImport() {
        return linkImport;
    }

    public void setLinkImport(LinkImport linkImport) {
        this.linkImport = linkImport;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

