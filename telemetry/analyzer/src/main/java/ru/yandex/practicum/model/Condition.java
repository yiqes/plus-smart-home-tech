package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.model.enums.ScenarioConditionOperation;
import ru.yandex.practicum.model.enums.ScenarioConditionType;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    @ManyToOne(cascade = CascadeType.ALL)
    Sensor sensor;
    @Enumerated(EnumType.STRING)
    ScenarioConditionType type;
    @Enumerated(EnumType.STRING)
    ScenarioConditionOperation operation;
    Integer value;
    @ManyToOne(cascade = CascadeType.ALL)
    Scenario scenario;
}