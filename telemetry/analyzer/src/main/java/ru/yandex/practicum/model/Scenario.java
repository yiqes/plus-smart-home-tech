package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "scenarios")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String hubId;
    String name;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL)
    List<ScenarioCondition> conditions;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL)
    List<ScenarioAction> actions;
}
