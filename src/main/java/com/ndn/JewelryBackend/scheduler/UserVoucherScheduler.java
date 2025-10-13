package com.ndn.JewelryBackend.scheduler;

import com.ndn.JewelryBackend.entity.UserVoucher;
import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import com.ndn.JewelryBackend.repository.UserVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserVoucherScheduler {

    private final UserVoucherRepository userVoucherRepository;

    @Scheduled(cron = "0 0 0 * * *") // chạy mỗi ngày 00:00
    @Transactional
    public void expireUserVouchers() {
        LocalDateTime now = LocalDateTime.now();

        List<UserVoucher> vouchers = userVoucherRepository.findAll();

        for (UserVoucher uv : vouchers) {
            if (uv.getExpiredAt() != null
                    && uv.getExpiredAt().isBefore(now)
                    && uv.getStatus() == UserVoucherStatus.ACTIVE) {
                uv.setStatus(UserVoucherStatus.EXPIRED);
            }
        }

        userVoucherRepository.saveAll(vouchers);
    }
}
