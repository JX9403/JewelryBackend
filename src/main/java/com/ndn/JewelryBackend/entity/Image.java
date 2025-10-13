package com.ndn.JewelryBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String url;

//    Nuwa co tu chuyen ve kieu phu hop
    @Column(name = "image_embedding", columnDefinition = "vector(512)", insertable = false, updatable = false)
    private Object imageEmbedding;
}
