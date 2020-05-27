package cn.chm.o2o.web.local;

import cn.chm.o2o.dto.LocalAuthExecution;
import cn.chm.o2o.entity.LocalAuth;
import cn.chm.o2o.entity.PersonInfo;
import cn.chm.o2o.enums.LocalAuthStateEnum;
import cn.chm.o2o.service.LocalAuthService;
import cn.chm.o2o.util.CodeUtil;
import cn.chm.o2o.util.HttpServletRequestUtil;
import cn.chm.o2o.util.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地账号系统的controller层
 */
@Controller
@RequestMapping(value = "/local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 注册本地账号
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/localregister", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> localRegister(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        LocalAuth localAuth = null;
        // 获取前端的账号信息
        String localAuthStr = HttpServletRequestUtil.getString(request,
                "localAuthStr");
        // 获取用户图片
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartFile profileImg = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            profileImg = (CommonsMultipartFile) multipartRequest
                    .getFile("thumbnail");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        try {
            localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (localAuth != null && localAuth.getPassword() != null
                && localAuth.getUsername() != null) {
            int userType = HttpServletRequestUtil.getInt(request, "usertype");
            try {
                localAuth.getPersonInfo().setUserType(userType);
                LocalAuthExecution le = localAuthService.register(localAuth,
                        profileImg);
                if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入注册信息");
        }
        return modelMap;
    }

    /**
     * 修改本地账号密码
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 获取账号
        String username = HttpServletRequestUtil.getString(request, "username");
        // 获取原密码
        String password = HttpServletRequestUtil.getString(request, "password");
        // 获取新密码
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        // 从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        // 判空，而且新密码和旧密码不能一样
        if (username != null && password != null
                && newPassword != null && user != null && user.getUserId() != null
                && !newPassword.equals(password)) {
            try {
                // 查看登录账号与修改的账号是否一致，不一致认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUsername().equals(username)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "修改账号与登录账号不一致，请重新登录");
                    return modelMap;
                }
                // 修改用户密码
                LocalAuthExecution localAuthExecution = localAuthService
                        .modifyLocalAuth(user.getUserId(), username, password, newPassword);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                    return modelMap;
                }
            } catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入新密码");
        }
        return modelMap;
    }

    /**
     * 登录验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 是否需要验证码校验
        boolean needVerify = HttpServletRequestUtil.getBoolean(request,
                "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 获取输入的账户数据
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        if (username != null && password != null) {
            // 获取数据库中的账户信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPws(username, password);
            if (localAuth != null) {
                // 若能获取到用户信息，则登录成功
                modelMap.put("success", true);
                modelMap.put("usertype", localAuth.getPersonInfo().getUserType());
                // 在session中设置用户信息
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logoutCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user", null);
        request.getSession().setAttribute("shopList", null);
        request.getSession().setAttribute("currentShop", null);
        modelMap.put("success", true);
        return modelMap;
    }

}
