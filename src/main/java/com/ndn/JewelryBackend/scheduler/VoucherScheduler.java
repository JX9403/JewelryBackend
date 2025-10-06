package com.ndn.JewelryBackend.scheduler;

import com.ndn.JewelryBackend.entity.Voucher;
import com.ndn.JewelryBackend.enums.VoucherStatus;
import com.ndn.JewelryBackend.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VoucherScheduler {

    private final VoucherRepository voucherRepository;

    // chạy mỗi ngày lúc 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void expireVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Voucher v : vouchers) {
            if (v.getExpiredAt() != null
                    && v.getExpiredAt().isBefore(now)
                    && v.getStatus() == VoucherStatus.ACTIVE) {
                v.setStatus(VoucherStatus.EXPIRED);
                voucherRepository.save(v);
            }
        }
    }
}
