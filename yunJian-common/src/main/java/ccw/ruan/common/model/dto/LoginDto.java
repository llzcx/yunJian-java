package ccw.ruan.common.model.dto;


import lombok.Data;
import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
public class LoginDto implements Serializable {
    public String username;
    public String password;
}
