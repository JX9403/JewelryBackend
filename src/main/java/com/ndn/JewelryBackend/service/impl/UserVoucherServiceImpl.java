package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.UserVoucherResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.entity.UserVoucher;
import com.ndn.JewelryBackend.entity.Voucher;
import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import com.ndn.JewelryBackend.enums.VoucherStatus;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.repository.UserVoucherRepository;
import com.ndn.JewelryBackend.repository.VoucherRepository;
import com.ndn.JewelryBackend.service.UserVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVoucherServiceImpl implements UserVoucherService {

    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final UserVoucherRepository userVoucherRepository;

    @Override
    public UserVoucherResponse sendVoucherToUser(Long voucherId, Long userId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        if (voucher.getStatus() != VoucherStatus.ACTIVE) {
            throw new IllegalStateException("Voucher not active!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean alreadySent = userVoucherRepository.existsByUserIdAndVoucherId(userId, voucherId);
        if (alreadySent) {
            throw new IllegalArgumentException("Voucher sent!");
        }

        LocalDateTime now = LocalDateTime.now();

        UserVoucher userVoucher = UserVoucher.builder()
                .user(user)
                .voucher(voucher)
                .sentAt(now)
                .expiredAt(now.plusMonths(1))
                .status(UserVoucherStatus.ACTIVE)
                .build();

        userVoucherRepository.save(userVoucher);

        return toResponse(userVoucher);
    }

    @Override
    public Page<UserVoucherResponse> getVouchersByUser(Long userId, UserVoucherStatus status, Pageable pageable) {
        Page<UserVoucher> page = userVoucherRepository.findByUserIdAndOptionalStatus(userId, status, pageable);
        return page.map(this::toResponse);
    }


    @Override
    public Page<UserVoucherResponse> getAllUserVouchers(Long userId, Long voucherId, UserVoucherStatus status, Pageable pageable) {
        UserVoucherStatus statusEnum = null;

        if (userId != null && !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        if (voucherId != null && !voucherRepository.existsById(voucherId)) {
            throw new ResourceNotFoundException("Voucher not found");
        }
        Page<UserVoucher> page = userVoucherRepository.searchUserVouchers(userId, voucherId, status, pageable);

        return page.map(this::toResponse);
    }


    @Override
    public void markAsUsed(Long userVoucherId) {
        UserVoucher userVoucher = userVoucherRepository.findById(userVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("User voucher not found"));

        userVoucher.setStatus(UserVoucherStatus.USED);
        userVoucher.setUsedAt(LocalDateTime.now());
        userVoucherRepository.save(userVoucher);
    }

    private UserVoucherResponse toResponse(UserVoucher uv) {
        return UserVoucherResponse.builder()
                .id(uv.getId())
                .voucherCode(uv.getVoucher().getCode())
                .userId(uv.getUser().getId())
                .voucherId(uv.getVoucher().getId())
                .description(uv.getVoucher().getDescription())
                .sentAt(uv.getSentAt())
                .expiredAt(uv.getExpiredAt())
                .usedAt(uv.getUsedAt())
                .status(uv.getStatus())
                .build();
    }
}
