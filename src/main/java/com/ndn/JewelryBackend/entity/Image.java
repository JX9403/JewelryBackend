package com.ndn.JewelryBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String url;

    @Column(name = "image_embedding", columnDefinition = "vector(512)")
    private String imageEmbedding;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
