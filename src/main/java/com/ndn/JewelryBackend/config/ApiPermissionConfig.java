package com.ndn.JewelryBackend.config;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ApiPermissionConfig {

    public String[] getPermitAllPatterns() {
        return new String[]{
                "/",
                "/images/**",
                "/index.html",
                "/api/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**"
        };
    }

    public String[] getPermitAllGet() {
        return new String[]{
                "/api/products/**",
                "/api/categories/**",
                "/api/collections/**"
        };
    }

    public String[] getPermitAllPost() {
        return new String[]{
                // thêm endpoint POST cho phép public ở đây
        };
    }

    public String[] getPermitAllPut() {
        return new String[]{
                // thêm endpoint PUT public ở đây
        };
    }

    public String[] getPermitAllDelete() {
        return new String[]{
                // thêm endpoint DELETE public ở đây
        };
    }
}
