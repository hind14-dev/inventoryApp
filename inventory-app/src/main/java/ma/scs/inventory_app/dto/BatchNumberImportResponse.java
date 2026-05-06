package ma.scs.inventory_app.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchNumberImportResponse {
    private String message;
    private BatchNumberImportResult result;
}
