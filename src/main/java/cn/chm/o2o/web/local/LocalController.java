package cn.chm.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/local")
public class LocalController {

    /**
     * 登录页面的路由
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login(){
        return "/local/login";
    }

    @RequestMapping(value = "/changepsw", method = RequestMethod.GET)
    private String changepwd(){
        return "/local/changepsw";
    }

}
