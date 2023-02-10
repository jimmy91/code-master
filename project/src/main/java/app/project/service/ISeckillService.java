package app.project.service;

import app.project.entity.seckill.SeckillEntity;
import utils.generator.common.dao.vo.CommonResult;
import java.util.List;

public interface ISeckillService {

	/**
	 * 查询全部的秒杀记录
	 * @return
	 */
	List<SeckillEntity> getSeckillList();

	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	SeckillEntity getById(long seckillId);
	/**
	 * 查询秒杀售卖商品
	 * @param seckillId
	 * @return
	 */
	Integer getSeckillCount(long seckillId);
	/**
	 * 删除秒杀售卖商品记录
	 * @param seckillId
	 * @return
	 */
	void deleteSeckill(long seckillId);

	/**
	 * 秒杀 一、会出现数量错误
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckil(long seckillId, long userId);

	/**
	 * 秒杀 二、程序锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckilLock(long seckillId,long userId);
	/**
	 * 秒杀 二、程序锁AOP
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckilAopLock(long seckillId,long userId);

	/**
	 * 秒杀 二、数据库悲观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckilDBPCC_ONE(long seckillId,long userId);
	/**
	 * 秒杀 三、数据库悲观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckilDBPCC_TWO(long seckillId,long userId);
	/**
	 * 秒杀 三、数据库乐观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	CommonResult startSeckilDBOCC(long seckillId,long userId,long number);
    
}
