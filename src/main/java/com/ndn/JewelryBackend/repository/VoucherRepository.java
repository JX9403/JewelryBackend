package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Voucher;
import com.ndn.JewelryBackend.enums.VoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByStatus(VoucherStatus status);
    Optional<Voucher> findByCode(String code);
    Page<Voucher> findByCodeContainingIgnoreCase(String code, Pageable pageable);
    Page<Voucher> findByStatus(VoucherStatus status, Pageable pageable);
    Page<Voucher> findByCodeContainingIgnoreCaseAndStatus(String code, VoucherStatus status, Pageable pageable);
    boolean existsByCode(String code);
}
