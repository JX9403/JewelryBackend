package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.response.UserVoucherResponse;
import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import com.ndn.JewelryBackend.service.UserVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-vouchers")
@RequiredArgsConstructor
public class UserVoucherController {

    private final UserVoucherService userVoucherService;

    @PostMapping("/send")
    public ResponseEntity<UserVoucherResponse> sendVoucher(
            @RequestParam Long voucherId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(userVoucherService.sendVoucherToUser(voucherId, userId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserVoucherResponse>> getByUserAndStatus(
            @RequestParam Long userId,
            @RequestParam(required = false) UserVoucherStatus status
    ) {
        return ResponseEntity.ok(userVoucherService.getVouchersByUser(userId, status));
    }


    @PutMapping("/{userVoucherId}/use")
    public ResponseEntity<Void> markAsUsed(@PathVariable Long userVoucherId) {
        userVoucherService.markAsUsed(userVoucherId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<UserVoucherResponse>> getAllUserVouchers(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long voucherId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sentAt,desc") String sort
    ) {
        // Tách tên cột và hướng sắp xếp
        String[] parts = sort.split(",");
        String sortField = parts[0];
        String sortDir = (parts.length > 1) ? parts[1] : "desc";

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<UserVoucherResponse> result = userVoucherService.getAllUserVouchers(userId, voucherId, status, pageable);
        return ResponseEntity.ok(result);
    }

}
