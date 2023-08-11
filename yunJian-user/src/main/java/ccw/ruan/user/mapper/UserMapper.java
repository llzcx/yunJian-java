package ccw.ruan.user.mapper;



import ccw.ruan.common.model.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 陈翔
 * @since 2023-06-10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取HR用户信息
     * @param userId 面试官id
     * @return
     */
    User getParentUser(@Param("userId") Integer userId);

}
