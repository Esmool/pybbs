package cn.tomoya.module.collect;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.tomoya.common.BaseModel;

/**
 * Created by tomoya. Copyright (c) 2016, All Rights Reserved. http://tomoya.cn
 */
public class Collect extends BaseModel<Collect> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2744615751564333549L;
	public static final Collect me = new Collect();

	/**
	 * 根据话题id与用户查询收藏记录
	 * 
	 * @param tid
	 * @param uid
	 * @return
	 */
	public Collect findByTidAndUid(Integer tid, Integer uid) {
		return super.findFirst("select * from pybbs_collect where tid = ? and uid = ?", tid, uid);
	}

	/**
	 * 查询话题被收藏的数量
	 * 
	 * @param tid
	 * @return
	 */
	public Long countByTid(Integer tid) {
		return this.findFirst("select count(1) as count from pybbs_collect where tid = ?", tid).getLong("count");
	}

	/**
	 * 收藏话题列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param uid
	 * @return
	 */
	public Page<Collect> findByUid(Integer pageNumber, Integer pageSize, Integer uid) {
		return this.paginate(pageNumber, pageSize, "select c.*, t.* ",
					" from pybbs_collect c left join pybbs_topic t on c.tid = t.id where t.isdelete = ? and c.uid = ?",
					false, uid);
	}

	/**
	 * 查询用户收藏的话题列表
	 * 
	 * @param uid
	 * @return
	 */
	public List<Collect> findByUid(int uid) {
		return this.find(
				"select t.* from pybbs_collect c left join pybbs_topic t on c.tid = t.id where t.isdelete = ? and c.uid = ? order by c.in_time desc",
				false, uid);
	}

	/**
	 * 查询用户收藏的数量
	 * 
	 * @param uid
	 * @return
	 */
	public Long countByUid(Integer uid) {
		return this.findFirst("select count(1) as count from pybbs_collect where uid = ?", uid).getLong("count");
	}

}
