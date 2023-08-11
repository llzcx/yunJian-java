package ccw.ruan.common.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 陈翔
 */
@EqualsAndHashCode
@Data
public class ESVo<T> {
    /**
     * 简历数据
     */
    List<T> list;
    /**
     * 总数据量
     */
    Integer total;
    /**
     * 页码
     */
    Integer pageNum;
    /**
     * 每页大小
     */
    Integer pageSize;

    public ESVo(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = Math.toIntExact(total);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    public ESVo(List<T> list, Integer total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
