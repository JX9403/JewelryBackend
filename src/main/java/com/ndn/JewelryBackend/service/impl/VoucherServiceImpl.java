package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.VoucherRequest;
import com.ndn.JewelryBackend.dto.response.VoucherResponse;
import com.ndn.JewelryBackend.entity.Voucher;
import com.ndn.JewelryBackend.enums.VoucherStatus;
import com.ndn.JewelryBackend.repository.VoucherRepository;
import com.ndn.JewelryBackend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        Voucher voucher = Voucher.builder()
                .code(request.getCode())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .status(VoucherStatus.DRAFT)
                .sentAt(null)
                .expiredAt(null)
                .build();
        voucherRepository.save(voucher);
        return mapToResponse(voucher);
    }

    @Override
    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucher.setCode(request.getCode());
        voucher.setDescription(request.getDescription());
        voucher.setQuantity(request.getQuantity());
        voucherRepository.save(voucher);
        return mapToResponse(voucher);
    }

    @Override
    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        return mapToResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VoucherResponse sendVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucher.setSentAt(LocalDateTime.now());
        voucher.setExpiredAt(voucher.getSentAt().plusMonths(1));
        voucher.setStatus(VoucherStatus.ACTIVE);
        voucherRepository.save(voucher);
        return mapToResponse(voucher);
    }

    private VoucherResponse mapToResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .description(voucher.getDescription())
                .quantity(voucher.getQuantity())
                .status(voucher.getStatus())
                .sentAt(voucher.getSentAt())
                .expiredAt(voucher.getExpiredAt())
                .build();
    }
}
