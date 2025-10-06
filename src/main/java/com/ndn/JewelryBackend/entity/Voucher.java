package com.ndn.JewelryBackend.entity;

import com.ndn.JewelryBackend.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

    private LocalDateTime sentAt;

    private LocalDateTime expiredAt;

}
