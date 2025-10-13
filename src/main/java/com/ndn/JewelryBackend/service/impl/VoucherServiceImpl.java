package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.VoucherRequest;
import com.ndn.JewelryBackend.dto.response.UserVoucherResponse;
import com.ndn.JewelryBackend.dto.response.VoucherResponse;
import com.ndn.JewelryBackend.entity.User;
import com.ndn.JewelryBackend.entity.UserVoucher;
import com.ndn.JewelryBackend.entity.Voucher;
import com.ndn.JewelryBackend.enums.VoucherStatus;
import com.ndn.JewelryBackend.exception.ResourceNotFoundException;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.repository.UserVoucherRepository;
import com.ndn.JewelryBackend.repository.VoucherRepository;
import com.ndn.JewelryBackend.service.VoucherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final UserVoucherRepository userVoucherRepository;

    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        boolean exists = voucherRepository.existsByCode(request.getCode());
        if (exists) {
            throw new IllegalArgumentException("Voucher code đã tồn tại: " + request.getCode());
        }
        Voucher voucher = Voucher.builder()
                .code(request.getCode())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .status(VoucherStatus.DRAFT)
                .build();
        voucherRepository.save(voucher);
        return mapToResponse(voucher);
    }

    @Override
    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        if (!voucher.getCode().equals(request.getCode())) {
            boolean exists = voucherRepository.existsByCode(request.getCode());
            if (exists) {
                throw new IllegalArgumentException("Voucher code đã tồn tại: " + request.getCode());
            }
            voucher.setCode(request.getCode());
        }
        voucher.setDescription(request.getDescription());
        voucher.setQuantity(request.getQuantity());
        voucher.setStatus(request.getStatus());
        voucherRepository.save(voucher);
        return mapToResponse(voucher);
    }


    @Override
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        voucherRepository.deleteById(id);
    }

    @Override
    public VoucherResponse getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        return mapToResponse(voucher);
    }

    @Override
    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        return mapToResponse(voucher);
    }

    @Override
    public Page<VoucherResponse> getAllVouchers( String status, Pageable pageable) {
        Page<Voucher> vouchers;

        VoucherStatus statusEnum = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = VoucherStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status not found");
            }
        }

       if (statusEnum != null) {
            vouchers = voucherRepository.findByStatus(statusEnum, pageable);
        } else {
            vouchers = voucherRepository.findAll(pageable);
        }

        return vouchers.map(this::mapToResponse);
    }



    private VoucherResponse mapToResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .description(voucher.getDescription())
                .quantity(voucher.getQuantity())
                .status(voucher.getStatus())
                .build();
    }
}
