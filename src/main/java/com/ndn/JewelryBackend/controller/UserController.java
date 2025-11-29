package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.CategoryResponse;
import com.ndn.JewelryBackend.dto.response.UserResponse;
import com.ndn.JewelryBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort

    ) {
        String[] parts = sort.split(",");
        String sortField = parts[0];
        String sortDir = (parts.length > 1) ? parts[1] : "desc";

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<UserResponse> result =
                userService.getAllUsers(email, pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(result)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
