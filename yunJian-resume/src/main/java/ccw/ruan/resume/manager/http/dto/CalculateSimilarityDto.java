package ccw.ruan.resume.manager.http.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
public class CalculateSimilarityDto implements Serializable {
    private String resume1;
    private String resume2;

    public CalculateSimilarityDto(String resume1, String resume2) {
        this.resume1 = resume1;
        this.resume2 = resume2;
    }

    public CalculateSimilarityDto() {
    }
}
