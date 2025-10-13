package com.ndn.JewelryBackend.entity;

import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_vouchers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    private LocalDateTime sentAt;
    private LocalDateTime expiredAt;
    private LocalDateTime usedAt; // chỉ cần thêm nếu bạn muốn biết “đã dùng lúc nào”

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserVoucherStatus status = UserVoucherStatus.ACTIVE;
}
