package ccw.ruan.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LogVo implements Serializable {
    private LocalDateTime time;
    private String active;
    private String detail;
}
