package ccw.ruan.resume.mapper;

import ccw.ruan.common.model.pojo.TalentPortrait;
import ccw.ruan.common.model.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 周威宇
 */
@Mapper
public interface TalentPortraitMapper extends BaseMapper<TalentPortrait> {
    /**
     * 获取用户信息
     * @param name 名字
     * @return
     */
     TalentPortrait getTalentPortrait(@Param("name") String name);
}
