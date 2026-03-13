package com.rishi.AuthanticationApplication.dtos;

import java.util.List;

public record AdminDashboardResponse(
        long totalUsers,
        List<UserSummaryDto> users
) {
}

