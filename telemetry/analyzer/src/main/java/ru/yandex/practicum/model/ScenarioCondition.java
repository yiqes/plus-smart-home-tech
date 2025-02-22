package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "scenario_conditions")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "condition_id")
    Condition condition;
}