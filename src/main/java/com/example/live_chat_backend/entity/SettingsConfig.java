package com.example.live_chat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings_config")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SettingsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private String textSize; // small, medium, large
    private String darkMode; // light, dark, system
}