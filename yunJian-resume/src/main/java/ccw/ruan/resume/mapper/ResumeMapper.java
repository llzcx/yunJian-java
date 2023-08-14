package ccw.ruan.resume.mapper;


import ccw.ruan.common.model.pojo.Resume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @author 陈翔
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
    /**
     * 获取当前已经录入的简历
     * @param userId
     * @return
     */
    List<Resume> getResumeList(@Param("userId")Integer userId);

    @Override
    Resume selectById(@Param("resumeId") Serializable resumeId);


    Resume selectResume(@Param("resumeId")Integer resumeId);

}
