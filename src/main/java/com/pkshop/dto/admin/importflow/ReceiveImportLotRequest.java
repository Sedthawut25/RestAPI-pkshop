package com.pkshop.dto.admin.importflow;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReceiveImportLotRequest(
        @NotNull LocalDate arrivalDate
) {}
