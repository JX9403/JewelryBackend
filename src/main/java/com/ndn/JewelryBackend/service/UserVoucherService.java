package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.UserVoucherResponse;
import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserVoucherService {
    UserVoucherResponse sendVoucherToUser(Long voucherId, Long userId);
    List<UserVoucherResponse> getVouchersByUser(Long userId, UserVoucherStatus status);
    void markAsUsed(Long userVoucherId);
    Page<UserVoucherResponse> getAllUserVouchers(Long userId, Long voucherId, String status, Pageable pageable);
}
