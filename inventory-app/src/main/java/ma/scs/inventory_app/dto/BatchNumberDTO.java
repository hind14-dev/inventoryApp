package ma.scs.inventory_app.dto;


import lombok.Data;

@Data
public class BatchNumberDTO {
    private Long id;
    private String bn;
    private boolean isDefaultBn;
    private Long partNumberId;
}

