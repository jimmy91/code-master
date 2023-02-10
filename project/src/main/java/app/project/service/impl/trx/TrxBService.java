package app.project.service.impl.trx;

import app.project.entity.trx.TrxDeptEntity;
import app.project.entity.trx.TrxEmpEntity;
import app.project.mapper.trx.TrxEmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Jimmy
 */
@Service
public class TrxBService {

    @Autowired
    private TrxEmpMapper empMapper;

    @Autowired
    private TrxAService aService;

    public void insertTrxEmpEntitys(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransaction(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 支持当前事务，如果当前没有事务，则新建事务
     * 如果当前存在事务，则加入当前事务，合并成一个事务
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionRequired(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
        }
    }

    /**
     * 新建事务，如果当前存在事务，则把当前事务挂起
     * 这个方法会独立提交事务，不受调用者的事务影响，父级异常，它也是正常提交
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionRequiredNew(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
        }
    }

    /**
     * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则新建事务
     * 如果当前存在事务，它将会成为父级事务的一个子事务，方法结束后并没有提交，只有等父事务结束才提交
     * 如果它异常，父级可以捕获它的异常而不进行回滚，正常提交
     * 但如果父级异常，它必然回滚，这就是和 REQUIRES_NEW 的区别
     * @param emps
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionNested(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 如果当前存在事务，则加入事务
     * 如果当前不存在事务，则以非事务方式运行，这个和不写没区别
     * @param emps
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionSupport(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 以非事务方式运行
     * 如果当前存在事务，则把当前事务挂起
     * @param emps
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionNotSupport(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 如果当前存在事务，则运行在当前事务中
     * 如果当前无事务，则抛出异常，也即父级方法必须有事务
     * @param emps
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionMandatory(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 以非事务方式运行，如果当前存在事务，则抛出异常
     * @param emps
     */
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysTransactionNever(List<TrxEmpEntity> emps){
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));
    /**
     * 测试异步任务会不会导致事务回滚
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertTrxEmpEntitysFuture(List<TrxEmpEntity> emps) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = () -> {
            int count = 0 ;
            for (TrxEmpEntity emp : emps) {
                empMapper.insert(emp);
                System.out.println("插入第 "+(++count) +" 条数据");
            }
            return count;
        };

        Future<Integer> future = threadPoolExecutor.submit(callable);
        future.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertTrxEmpEntitysFutureInner(List<TrxEmpEntity> emps) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = () -> {
            int count = 0 ;
            for (TrxEmpEntity emp : emps) {
                empMapper.insert(emp);
                System.out.println("插入第 "+(++count) +" 条数据");
                if(count == 6){
                    if(true) throw new IllegalArgumentException("出 bug 了");
                }
            }
            return count;
        };

        Future<Integer> future = threadPoolExecutor.submit(callable);
        future.get();
    }

    public void trx09(List<TrxEmpEntity> emps, TrxDeptEntity dept) {
        aService.trx09(dept);
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    public void trx10(List<TrxEmpEntity> emps, TrxDeptEntity dept) {
        aService.trx10(dept);
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
        }
    }
}
