package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.VoucherRequest;
import com.ndn.JewelryBackend.dto.response.VoucherResponse;

import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher(VoucherRequest request);
    VoucherResponse updateVoucher(Long id, VoucherRequest request);
    void deleteVoucher(Long id);
    VoucherResponse getVoucherById(Long id);
    List<VoucherResponse> getAllVouchers();
    VoucherResponse sendVoucher(Long id);
}
