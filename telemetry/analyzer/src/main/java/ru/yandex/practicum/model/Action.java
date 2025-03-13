package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.model.enums.ActionType;

@Entity
@Table(name = "actions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    Sensor sensor;
    @Enumerated(EnumType.STRING)
    ActionType type;
    Integer value;
    @ManyToOne(cascade = CascadeType.ALL)
    Scenario scenario;
}