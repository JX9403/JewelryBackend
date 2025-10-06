package com.ndn.JewelryBackend.entity;

import com.ndn.JewelryBackend.enums.ActiveStatus;
import com.ndn.JewelryBackend.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ActiveStatus status;
}
