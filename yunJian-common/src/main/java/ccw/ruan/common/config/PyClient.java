package ccw.ruan.common.config;

/**
 * R9000P phone：
 * 机房：
 * R9000P:10.100.174.171
 * 轻薄本：10.100.167.8
 * ZWY：10.100.15.166
 *
 * 热点：
 * R9000P:192.168.18.89
 * 轻薄本：192.168.18.9
 * ZWY：192.168.18.83
 * @author 陈翔
 */
public interface PyClient {
    String R9000P = "192.168.18.89:7070";
    String ZWY_IP = "192.168.18.83";
    String ZWY = "http://"+ZWY_IP+":7070";
    String ZWY1 = "http://"+ZWY_IP+":7071";
}
