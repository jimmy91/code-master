package app.project.service.impl.trx;

import app.project.entity.trx.TrxDeptEntity;
import app.project.entity.trx.TrxEmpEntity;
import app.project.mapper.trx.TrxDeptMapper;
import app.project.mapper.trx.TrxEmpMapper;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import utils.tools.mybatis.LambdaQueryWrapperX;
import utils.tools.random.RandomUtil;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Jimmy
 * https://www.zhihu.com/question/506754250/answer/2645488294?utm_id=0
 */
@Slf4j
@Service
public class TrxServiceImpl extends ServiceImpl<TrxEmpMapper, TrxEmpEntity> {

    @Resource
    private TrxDeptMapper deptMapper;

    @Resource
    private TrxEmpMapper empMapper;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Autowired
    private List<DataSource> dataSources;

    @Autowired
    private TrxAService aService;

    @Autowired
    private TrxBService bService;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));


    public TrxEmpEntity getEmp(Integer no) {
        return empMapper.selectEmpByNo(no);
    }

    public String noTrx() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertDept(dept, emps);
        return deptNo;
    }

    public String trx01() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertDeptTransaction(dept, emps);
        return deptNo;
    }

    public String trx02() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.insertSteps(dept, emps);
        return deptNo;
    }

    public String trx03() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);

        aService.transactionParallel(dept, emps);
        return deptNo;
    }

    public String trx04() throws ExecutionException, InterruptedException {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 10);

        aService.transactionAsyncInner(dept, emps);
        return deptNo;
    }

    @Transactional
    public String trx05() throws InterruptedException {
        TrxEmpEntity emp = new TrxEmpEntity();
        Integer empno = 10000 + cn.hutool.core.util.RandomUtil.randomInt(10000);
        emp.setEmpno(empno);
        emp.setEname("事务trx05");
        empMapper.insert(emp);

        Thread thread = new Thread() {
            @Override
            public void run() {
                TrxEmpEntity one = empMapper.selectById(empno);
                log.info("事务外查询，查询结果为: >>>>>> " + one);
            }
        };
        thread.start();
        TrxEmpEntity one = empMapper.selectById(empno);
        log.info("事务内查询，查询结果为: >>>>>> " + one);
        // 等待线程执行完毕
        thread.join();

        // 抛出异常阻止数据插入
        throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional
    public String trx06() throws InterruptedException {
        TrxEmpEntity emp = new TrxEmpEntity();
        Integer empno = 10000 + cn.hutool.core.util.RandomUtil.randomInt(10000);
        emp.setEmpno(empno);
        emp.setEname("事务trx06");
        empMapper.insert(emp);

        final Thread[] thread = {null};

        // 注入为提交事务后查询
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                thread[0] = new Thread() {
                    @Override
                    public void run() {
                        // 这个线程会在方法结束并提交事务后才会执行
                        TrxEmpEntity one = empMapper.selectById(empno);
                        log.info("查询结果为:" + one);
                    }
                };
                thread[0].start();

                // 等待线程执行完成
                try {
                    thread[0].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 抛出异常阻止数据插入
                throw new IllegalArgumentException("出 bug 了");
            }
        });
        return empno.toString();
    }

    public String trx07() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(transactionDefinition);
        try {
            deptMapper.insert(dept);
            int i = 1 / 0;
            txManager.commit(status);
        } catch (Exception e) {
            log.error("异常准备回滚");
            txManager.rollback(status);
        }
        return deptNo;
    }

    public String trx08() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);
        // 无法实现事务，需要通过代理实现，对比trx03()事务
        transactionParallel(dept, emps);
        return deptNo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionParallel(TrxDeptEntity dept, List<TrxEmpEntity> emps) {
        deptMapper.insert(dept);
        bService.insertTrxEmpEntitysTransactionRequired(emps);
        if (true) throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional(rollbackFor = Exception.class)
    public String trx09() {
        String deptNo = randomDeptNo();
        TrxDeptEntity dept = provideDept(deptNo);
        List<TrxEmpEntity> emps = provideEmps(deptNo, 5);
        aService.trx09(dept, emps);
        return deptNo;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public String trx10() throws ExecutionException, InterruptedException, TimeoutException {
        String deptNo = randomDeptNo();
        List<TrxEmpEntity> empEntities = Collections.emptyList();
        // 读已提交情况下，如果不预先进行范围查询，那后续就可以获取到最新数据，没有加间隙锁
        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
              .between(TrxEmpEntity::getEmpno, 50600, 59031));
       // log.info(">>>>>> 初始范围条数 count={}", empEntities.size());
        final Integer updateEmpNo = 7104;
        // 新事务插入数据
        final Integer innerEmpNo = 51658;
        // 启动一个新的线程添加数据
        Callable<TrxEmpEntity> callable = () -> {
            // 添加一个事务范围内的数据
            TrxEmpEntity emp = new TrxEmpEntity();
            emp.setDeptno(deptNo);
            emp.setEname(RandomUtil.username());
            emp.setEmpno(innerEmpNo);
            empMapper.insert(emp);
            // 更新一条数据
            String rName = RandomUtil.username();
            TrxEmpEntity updateEmp = new TrxEmpEntity();
            updateEmp.setEmpno(updateEmpNo);
            updateEmp.setEname(rName);
            empMapper.updateById(updateEmp);

            List<TrxEmpEntity> newEmps = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                    .between(TrxEmpEntity::getEmpno, 50600, 59031));
            log.info(">>>>>> 异步线程获取到新添加的数据 count={}, 更新某用户名称 rName={}", newEmps.size(), rName);
            return emp;
        };
        Future<TrxEmpEntity> future = threadPoolExecutor.submit(callable);
        TrxEmpEntity addEmp = future.get(5, TimeUnit.SECONDS);
        log.info(">>>>>> 添加成功 {}", addEmp);
        ThreadUtil.sleep(100);

        String rName = empMapper.selectEmpByNo(updateEmpNo).getEname();
        TrxEmpEntity insertInnerEntity = empMapper.selectEmpByNo(innerEmpNo);
        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600, 59031));
        List<TrxEmpEntity> empEntitiesV2 = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600 - 5, 59031 + 5));
        log.info(">>>>>> (读已提交隔离级别下)能够查询到更新的数据，可重复读隔离级别不可查询到更新后的数据。 rName = {}", rName);
        log.info(">>>>>> (读已提交隔离级别下)能够通过ID查询到数据，可重复读隔离级别查询结果为空。insertInnerEntity = {}", insertInnerEntity);
        log.info(">>>>>> (读已提交隔离级别下)无法通过相同的范围查询获取到新添加的数据，但改范围可以查询到新添加数据(count不等于countV2)" +
                "。可重复读隔离级别都读不到添加数据 count=countV2， count = {} countV2 = {} ", empEntities.size(), empEntitiesV2.size());

        // 事务内添加数据
        TrxEmpEntity emp = new TrxEmpEntity();
        emp.setDeptno(deptNo);
        emp.setEname(RandomUtil.username());
        emp.setEmpno(innerEmpNo+1);
        empMapper.insert(emp);

        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 50600, 59031));
        log.info(">>>>>> 内部进行一次insert后，(读已提交隔离级别下)相同的范围查询能获取其它事务新添加的数据，可重复读隔离级别还是获取不到 count={} ", empEntities.size());
        return deptNo;
    }


    private List<TrxEmpEntity> provideEmps(String deptNo, int count) {
        List<TrxEmpEntity> emps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TrxEmpEntity emp = new TrxEmpEntity();
            emp.setDeptno(deptNo);
            emp.setEname(RandomUtil.username());
            emp.setEmpno(RandomUtils.nextInt(10, 100000));
            emp.setJob(RandomUtil.job());
            emp.setHiredate(RandomUtil.date());
            emp.setMgr(RandomUtils.nextInt(10, 1000));
            emp.setSal(new BigDecimal(RandomUtils.nextDouble(1000, 100000)));
            emp.setComm(new BigDecimal(RandomUtils.nextDouble(0, 1000)));
            emps.add(emp);
        }
        return emps;
    }

    private TrxDeptEntity provideDept(String deptNo) {
        TrxDeptEntity dept = new TrxDeptEntity();
        dept.setDname(RandomUtil.username());
        dept.setLoc(RandomUtil.address());
        dept.setDeptno(deptNo);
        return dept;
    }

    @NotNull
    private static String randomDeptNo() {
        String deptNo = String.format("%s#%05d", DateUtil.today(), RandomUtil.randomNumber(10000));
        log.info("部门号：deptNo >>>>>>> {}", deptNo);
        return deptNo;
    }


}
