package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.exception.InsufficientStockException;
import com.pgvector.PGvector;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {
    PGvector productAnalyze(MultipartFile file) throws InsufficientStockException;
}

