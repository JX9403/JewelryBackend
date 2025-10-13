package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.UserVoucher;
import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {

    boolean existsByUserIdAndVoucherId(Long userId, Long voucherId);


    @Query("""
        SELECT uv FROM UserVoucher uv
        WHERE uv.user.id = :userId
          AND (:status IS NULL OR uv.status = :status)
        ORDER BY uv.sentAt DESC
    """)
    List<UserVoucher> findByUserIdAndOptionalStatus(
            @Param("userId") Long userId,
            @Param("status") UserVoucherStatus status
    );

    @Query("""
        SELECT uv FROM UserVoucher uv
        WHERE (:userId IS NULL OR uv.user.id = :userId)
          AND (:voucherId IS NULL OR uv.voucher.id = :voucherId)
          AND (:status IS NULL OR uv.status = :status)
    """)
    Page<UserVoucher> searchUserVouchers(
            @Param("userId") Long userId,
            @Param("voucherId") Long voucherId,
            @Param("status") UserVoucherStatus status,
            Pageable pageable
    );
}
