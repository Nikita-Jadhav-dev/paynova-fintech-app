package com.paynova.app.service;

import com.paynova.app.dto.response.AdminStatsResponse;

public interface AdminService {
    AdminStatsResponse getSystemStats();
}
