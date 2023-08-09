package ccw.ruan.common.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 陈翔
 */
@Data
public class TokenPair implements Serializable {
    private String accessToken;
    private String refreshToken;

    public TokenPair() {
    }

    public TokenPair(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "TokenPair{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
