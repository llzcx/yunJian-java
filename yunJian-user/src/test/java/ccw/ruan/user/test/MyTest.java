package ccw.ruan.user.test;
 
import ccw.ruan.common.model.dto.RegisterDto;
import ccw.ruan.user.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
 
   @Autowired
   UserServiceImpl userService;



 
    @Test
    public void es()
    {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("123");
        registerDto.setPassword("123");
        registerDto.setEmail("123@163.com");
        userService.register(registerDto);
    }

}