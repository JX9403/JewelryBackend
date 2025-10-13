package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.VoucherRequest;
import com.ndn.JewelryBackend.dto.response.VoucherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher(VoucherRequest request);
    VoucherResponse updateVoucher(Long id, VoucherRequest request);
    void deleteVoucher(Long id);
    VoucherResponse getVoucherByCode(String code);
    VoucherResponse getVoucherById(Long id);
    Page<VoucherResponse> getAllVouchers( String status, Pageable pageable) ;
}
