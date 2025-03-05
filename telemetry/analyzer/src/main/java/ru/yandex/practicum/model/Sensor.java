package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String hubId;
    String type;
}