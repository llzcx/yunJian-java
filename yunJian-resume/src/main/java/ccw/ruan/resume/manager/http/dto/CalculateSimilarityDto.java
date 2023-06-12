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
}
