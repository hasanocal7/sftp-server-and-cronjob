package com.example.task.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "json_field", columnDefinition = "TEXT")
    private String serverInfos;
    @Column(name = "json_name", unique = true)
    private String jsonFileName;
}
