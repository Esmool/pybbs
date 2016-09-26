package cn.tomoya.module.section;

import java.util.List;

import cn.tomoya.common.BaseModel;

/**
 * Created by tomoya. Copyright (c) 2016, All Rights Reserved. http://tomoya.cn
 */
public class Section extends BaseModel<Section> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7843955124210521914L;
	public static final Section me = new Section();

	public List<Section> findAll() {
		return super.find("select * from pybbs_section");
	}

	public List<Section> findByShowStatus(boolean isshow) {
		return this.find("select * from pybbs_section where show_status = ? order by display_index", isshow);
	}

	public Section findByTab(String tab) {
		return findFirst("select * from pybbs_section where tab = ?", tab);
	}

	public String showStatus(Section section) {
		if (section.getBoolean("show_status")) {
			return "true";
		} else {
			return "false";
		}
	}

}
