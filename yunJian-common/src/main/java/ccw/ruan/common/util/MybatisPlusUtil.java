package ccw.ruan.common.util;


import ccw.ruan.common.exception.SystemException;
import ccw.ruan.common.request.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈翔
 */
public class MybatisPlusUtil {
    /**
     * 简化利用mp查询操作
     * @param object
     * @return
     */
    public static Map<String,Object> getMap(Object ...object) {
        if(object.length%2==1) {
            throw new SystemException(ResultCode.COMMON_FAIL);
        }else{
            Map<String,Object> mp = new HashMap<>();
            String key = null;
            for (int i = 0; i < object.length; i++) {
                if(i%2==0){
                    if(object[i] instanceof String){
                        key = (String) object[i];
                    }else{
                        throw new SystemException(ResultCode.COMMON_FAIL);
                    }
                }else{
                    mp.put(key, object[i]);
                }
            }
            return mp;
        }
    }

    /**
     * 简化利用QueryWrapperEq查询操作
     * @param object
     * @return
     */
    public static <T> QueryWrapper<T> queryWrapperEq(Object ...object) {
        if(object.length%2==1) {
            throw new SystemException(ResultCode.COMMON_FAIL);
        }else{
            QueryWrapper<T> queryWrapper = new QueryWrapper<>();
            String key = null;
            for (int i = 0; i < object.length; i++) {
                if(i%2==0){
                    if(object[i] instanceof String){
                        key = (String) object[i];
                    }else{
                        throw new SystemException(ResultCode.COMMON_FAIL);
                    }
                }else{
                    queryWrapper.eq(key, object[i]);
                }
            }
            return queryWrapper;
        }
    }

}
