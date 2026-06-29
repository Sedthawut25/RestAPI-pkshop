package com.pkshop.dto.admin.importflow;

import jakarta.validation.constraints.NotBlank;

public record CreateImportDocumentRequest(
        @NotBlank String docType // E_IMPORT, INVOICE, ...
) {}
