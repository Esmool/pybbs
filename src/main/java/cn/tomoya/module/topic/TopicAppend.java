package cn.tomoya.module.topic;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.tomoya.common.BaseModel;

/**
 * Created by tomoya. Copyright (c) 2016, All Rights Reserved. http://tomoya.cn
 */
public class TopicAppend extends BaseModel<TopicAppend> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470673394383608801L;
	public static final TopicAppend me = new TopicAppend();

	/**
	 * 查询话题追加内容
	 * 
	 * @param tid
	 * @return
	 */
	public List<TopicAppend> findByTid(Integer tid) {
		return this.find("select * from pybbs_topic_append where isdelete = ? and tid = ? order by in_time", false,
				tid);
	}

	/**
	 * 删除话题追加内容
	 * 
	 * @param tid
	 */
	public void deleteByTid(Integer tid) {
		Db.update("update pybbs_topic_append set isdelete = 1 where tid = ?", tid);
	}
}
