package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.VoucherRequest;
import com.ndn.JewelryBackend.dto.response.VoucherResponse;
import com.ndn.JewelryBackend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<VoucherResponse> createVoucher(@RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.createVoucher(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponse> updateVoucher(
            @PathVariable Long id,
            @RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.updateVoucher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherResponse> getVoucherById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }

    @GetMapping
    public ResponseEntity<List<VoucherResponse>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<VoucherResponse> sendVoucher(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.sendVoucher(id));
    }
}
