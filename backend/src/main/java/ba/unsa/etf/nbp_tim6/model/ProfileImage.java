package ba.unsa.etf.nbp_tim6.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileImage {
    private byte[] data;
    private String contentType;
    private String fileName;
}