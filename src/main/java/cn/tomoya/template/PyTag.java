package cn.tomoya.template;

import freemarker.template.SimpleHash;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
public class PyTag extends SimpleHash {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7586426067736505887L;

	@SuppressWarnings("deprecation")
	public PyTag() {
        put("hasPermission", new PermissionDirective());
        put("scores", new ScoresDirective());
    }
}
