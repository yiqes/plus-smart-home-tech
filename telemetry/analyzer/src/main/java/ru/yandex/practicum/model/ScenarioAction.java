package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "scenario_actions")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "action_id")
    Action action;
}