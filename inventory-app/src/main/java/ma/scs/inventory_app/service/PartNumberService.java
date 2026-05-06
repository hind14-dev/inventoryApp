package ma.scs.inventory_app.service;



import ma.scs.inventory_app.dto.PartNumberDTO;
import ma.scs.inventory_app.entities.PartNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import  ma.scs.inventory_app.dto.UploadResult;

import java.io.IOException;
import java.util.List;

public interface PartNumberService {

    Page<PartNumberDTO> getAllParts(Pageable pageable);
    PartNumber getPartById(Long id);
    PartNumber getPartByPn(String pn);
    PartNumber savePart(PartNumber partNumber);
    PartNumber updatePart(Long id, PartNumber updatedPart);
    void deletePart(Long id);
    public UploadResult processExcelFile(MultipartFile file) throws IOException;
}

