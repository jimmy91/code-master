package app.project.service.impl;

import app.annotation.ServiceLock;
import app.project.entity.SeckillEntity;
import app.project.entity.SuccessKilledEntity;
import app.project.mapper.SeckillMapper;
import app.project.mapper.SuccessKilledMapper;
import app.project.service.ISeckillService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.generator.common.dao.vo.CommonResult;
import utils.tools.mybatis.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jimmy
 */
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, SeckillEntity> implements ISeckillService {
    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
	 * 互斥锁 参数默认false，不公平锁
     */
	private Lock lock = new ReentrantLock(true);

	@Resource
	private SuccessKilledMapper successKilledMapper;

	@Override
	public List<SeckillEntity> getSeckillList() {
		return this.baseMapper.selectList(null);
	}

	@Override
	public SeckillEntity getById(long seckillId) {
		return this.baseMapper.selectById(seckillId);
	}

	@Override
	public Integer getSeckillCount(long seckillId) {
		return successKilledMapper.selectCount(new LambdaQueryWrapperX<SuccessKilledEntity>()
				.eq(SuccessKilledEntity::getSeckillId, seckillId));
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteSeckill(long seckillId) {
		this.successKilledMapper.delete(new LambdaQueryWrapperX<SuccessKilledEntity>().eq(SuccessKilledEntity::getSeckillId, seckillId));
		this.baseMapper.update(null, new LambdaUpdateWrapper<SeckillEntity>().set(SeckillEntity::getNumber, 100).set(SeckillEntity::getVersion, 0)
				.eq(SeckillEntity::getSeckillId, seckillId));
	}
	@Override
	@Transactional
	public CommonResult startSeckil(long seckillId, long userId) {
		//校验库存
		SeckillEntity entity =  getById(seckillId);
		Long number = entity.getNumber();
		if(number>0){
			//扣库存
			LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>().eq(SeckillEntity::getSeckillId, seckillId).setSql("`number`=`number`-1") ;
			update(null, uw);

			//创建订单
			SuccessKilledEntity killed = new SuccessKilledEntity();
			killed.setSeckillId(seckillId);
			killed.setUserId(userId);
			killed.setState((short)0);
			killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
			successKilledMapper.insert(killed);
			//支付
			return CommonResult.success(true);
		}else{
			return CommonResult.error(-1, "失败");
		}
	}
	@Override
	@Transactional
	public CommonResult  startSeckilLock(long seckillId, long userId) {
		 lock.lock();
		 try {
			//这里、不清楚为啥、总是会被超卖101、难道锁不起作用、lock是同一个对象
			//来自热心网友 zoain 的细心测试思考、然后自己总结了一下
			//事物未提交之前，锁已经释放(事物提交是在整个方法执行完)，导致另一个事物读取到了这个事物未提交的数据，也就是传说中的脏读。建议锁上移
			//给自己留个坑思考：为什么分布式锁(zk和redis)没有问题？难道不是事物的锅
			 SeckillEntity entity =  getById(seckillId);
			 Long number = entity.getNumber();
			 if(number>0){
				 //扣库存
				 LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>().eq(SeckillEntity::getSeckillId, seckillId).setSql("`number`=`number`-1") ;
				 update(null, uw);

				 //创建订单
				 SuccessKilledEntity killed = new SuccessKilledEntity();
				 killed.setSeckillId(seckillId);
				 killed.setUserId(userId);
				 killed.setState((short)0);
				 killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
				 successKilledMapper.insert(killed);
				 //支付
				 return CommonResult.success(true);
			 }else{
				 return CommonResult.error(-1, "失败");
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return CommonResult.success(true);
	}
	@Override
	@ServiceLock
	@Transactional
	public CommonResult startSeckilAopLock(long seckillId, long userId) {
		//来自码云码友<马丁的早晨>的建议 使用AOP + 锁实现
		SeckillEntity entity =  getById(seckillId);
		Long number = entity.getNumber();
		if(number>0){
			//扣库存
			LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>().eq(SeckillEntity::getSeckillId, seckillId).setSql("`number`=`number`-1") ;
			update(null, uw);

			//创建订单
			SuccessKilledEntity killed = new SuccessKilledEntity();
			killed.setSeckillId(seckillId);
			killed.setUserId(userId);
			killed.setState((short)0);
			killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
			successKilledMapper.insert(killed);
			//支付
			return CommonResult.success(true);
		}else{
			return CommonResult.error(-1, "失败");
		}
	}
	@Override
	@Transactional
	public CommonResult startSeckilDBPCC_ONE(long seckillId, long userId) {
		//单用户抢购一件商品或者多件都没有问题
		SeckillEntity entity =  this.baseMapper.selectForUpdate(seckillId);
		Long number = entity.getNumber();
		if(number>0){
			//扣库存
			LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>().eq(SeckillEntity::getSeckillId, seckillId).setSql("`number`=`number`-1") ;
			update(null, uw);

			//创建订单
			SuccessKilledEntity killed = new SuccessKilledEntity();
			killed.setSeckillId(seckillId);
			killed.setUserId(userId);
			killed.setState((short)0);
			killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
			successKilledMapper.insert(killed);
			//支付
			return CommonResult.success(true);
		}else{
			return CommonResult.error(-1, "失败");
		}
	}

	@Override
	@Transactional
	public CommonResult startSeckilDBPCC_TWO(long seckillId, long userId) {
		//单用户抢购一件商品没有问题、但是抢购多件商品不建议这种写法
		LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>().eq(SeckillEntity::getSeckillId, seckillId).gt(SeckillEntity::getNumber, 0)
				.setSql("`number`=`number`-1") ;
		if(update(null, uw)){
			//创建订单
			SuccessKilledEntity killed = new SuccessKilledEntity();
			killed.setSeckillId(seckillId);
			killed.setUserId(userId);
			killed.setState((short)0);
			killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
			successKilledMapper.insert(killed);
			//支付
			return CommonResult.success(true);
		}else{
			return CommonResult.error(-1, "失败");
		}
	}

	@Override
	@Transactional
	public CommonResult startSeckilDBOCC(long seckillId, long userId, long number) {
		SeckillEntity entity = this.baseMapper.selectById(seckillId);
		if(entity.getNumber()>=number){
			//乐观锁 扣库存
			LambdaUpdateWrapper uw = new LambdaUpdateWrapper<SeckillEntity>()
					.eq(SeckillEntity::getSeckillId, seckillId).eq(SeckillEntity::getVersion, entity.getVersion())
					.setSql("`number`=`number`-"+number + ", version=version+1");
			if(update(null, uw)){
				//创建订单
				SuccessKilledEntity killed = new SuccessKilledEntity();
				killed.setSeckillId(seckillId);
				killed.setUserId(userId);
				killed.setState((short)0);
				killed.setCreateTime(new Timestamp(System.currentTimeMillis()));
				successKilledMapper.insert(killed);
				//支付
				return CommonResult.success(true);
			}else{
				return CommonResult.error(-1, "失败");
			}
		}else{
			return CommonResult.error(-1, "失败");
		}
	}

}
