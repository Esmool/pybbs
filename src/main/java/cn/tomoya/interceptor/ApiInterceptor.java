package cn.tomoya.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.tomoya.module.user.User;
import cn.tomoya.utils.Result;
import cn.tomoya.utils.StrUtil;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
public class ApiInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        String token = controller.getPara("token");

        boolean flag = false;
        User user;
        String msg = "请先登录";
        if(StrUtil.notBlank(token)) {
            user = User.me.findByAccessToken(token);
            if(user != null) {
                if(user.getBoolean("isblock")) {
                    msg = "您的账户已被禁用";
                } else {
                    flag = true;
                }
            }
        }

        if(flag) {
            inv.invoke();
        } else {
            controller.renderJson(new Result("201", msg, null));
        }
    }

}
