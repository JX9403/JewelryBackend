package com.ndn.JewelryBackend.entity;

import com.ndn.JewelryBackend.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;

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

    private String url;
}
