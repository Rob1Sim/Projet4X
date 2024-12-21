package org.example.projet4dx.model;

import jakarta.persistence.*;

import java.io.Serializable;
@Entity
@Table(name = "Game")
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
