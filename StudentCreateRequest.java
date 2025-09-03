package com.example.scr.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StudentCreateRequest(
        @NotBlank String name,
        @NotEmpty List<Long> courseIds
) {}
