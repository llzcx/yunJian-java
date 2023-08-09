package ccw.ruan.user;


import ccw.ruan.common.model.pojo.InvitationTemplate;
import ccw.ruan.user.mapper.TemplateMapper;
import ccw.ruan.user.service.ITemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateTest {

    @Autowired
    ITemplateService templateService;

    @Test
    public void save(){
        final InvitationTemplate template = new InvitationTemplate();
        template.setTemplate("");
        templateService.save(template);
    }

    @Test
    public void sendInvitation(){
        try {
            templateService.sendInvitation(2,22);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
