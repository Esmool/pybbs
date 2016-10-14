package com.zjb.authorize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;

import cn.tomoya.common.Constants;
import cn.tomoya.module.system.Role;
import cn.tomoya.module.system.UserRole;
import cn.tomoya.module.user.User;
import cn.tomoya.utils.DateUtil;
import cn.tomoya.utils.StrUtil;
import cn.tomoya.utils.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/jumpIn")
public class AuthController extends Controller {

	public void index() {
		String path = this.getPara("section", "").trim();
        path = path.isEmpty() ? "/" : String.format("/?tab=%s", path);
        
        HttpServletRequest request = this.getRequest();
        HttpSession session = this.getSession();
        Object isOld = session.getAttribute("__isOld");
        if (isOld != null) {
            session.invalidate();
            this.redirect("/jumpIn");
            return;
        }
        
        session.setAttribute("__isOld", true);
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        
        if (principal != null) {
            String cid = principal.getName();
            Map<String, Object> attributes = principal.getAttributes();
            this.transAuthorization(cid, attributes);
        }
        
        this.redirect(path);
	}
	
	public void test() {
		String cid = "testUser";
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("username", "testUser");
		map.put("name", "测试帐号");
		map.put("avatar", "");
		
		this.transAuthorization(cid, map);
		this.redirect("/");
	}

	private void transAuthorization(String cid, Map<String, Object> attributes) {
		User u = User.me.findByThirdId(cid);
		if (u == null)
			u = this.createNewUser(cid);

		this.syncUserInfo(cid, u, attributes);
		this.simulateLoginStatus(u);
	}

	private void simulateLoginStatus(User u) {
		setCookie(Constants.USER_ACCESS_TOKEN,
				StrUtil.getEncryptionToken(u.getStr("access_token")), 30 * 24 * 60 * 60,
				"/", PropKit.get("cookie.domain"), true);
	}

	private void syncUserInfo(String cid, User u, Map<String, Object> attributes) {
		BiFunction<String, Object, Object> get = (n, d) -> attributes.get(n) == null ? d : attributes.get(n);
		u
			 .set("score", 0)
			 .set("realname", get.apply("name", ""))
			 .set("avatar", get.apply("avatar", ""))
			 .set("signature", get.apply("name", ""))
			 .update();
	}

	private User createNewUser(String cid) {
		Date now = new Date();
		User u = new User()
			.set("third_id", cid)
			.set("nickname", cid)
			.set("access_token", StrUtil.getUUID())
			.set("score", 0)
			.set("isblock", false)
            .set("channel", Constants.LoginEnum.CAS.name())
            .set("receive_msg", false)
            .set("in_time", now)
            .set("expire_time", DateUtil.getDateAfter(now, 3000));
		u.save();

		// 新注册的用户角色都是普通用户
		Role role = Role.me.findByName("user");
		if (role == null)
			return u;

		new UserRole()
			.set("uid", u.getInt("id"))
			.set("rid", role.getInt("id"))
			.save();

		return u;
	}

}
