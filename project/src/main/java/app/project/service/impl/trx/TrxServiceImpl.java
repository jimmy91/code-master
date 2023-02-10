package app.project.service.impl.trx;

import app.project.entity.trx.TrxDeptEntity;
import app.project.entity.trx.TrxEmpEntity;
import app.project.mapper.trx.TrxDeptMapper;
import app.project.mapper.trx.TrxEmpMapper;
import cn.hutool.core.date.DateUtil;
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


    public TrxEmpEntity getEmp(Long no) {
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
        List<TrxEmpEntity> empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 51600, 59031));
        log.info(">>>>>> 初始范围条数 count={}", empEntities.size());
        // 启动一个新的线程添加数据
        Callable<TrxEmpEntity> callable = () -> {
            TrxEmpEntity emp = new TrxEmpEntity();
            emp.setDeptno(deptNo);
            emp.setEname(RandomUtil.username());
            emp.setEmpno(RandomUtils.nextInt(10, 100000));
            emp.setEmpno(51621);
            empMapper.insert(emp);

            List<TrxEmpEntity> newEmps = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                    .between(TrxEmpEntity::getEmpno, 51600, 59031));
            log.info(">>>>>> count={}", newEmps.size());

            return emp;
        };
        Future<TrxEmpEntity> future = threadPoolExecutor.submit(callable);
        TrxEmpEntity addEmp = future.get(5, TimeUnit.SECONDS);
        log.info(">>>>>> 添加成功 {}", addEmp);

        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 51600, 59031));
        log.info(">>>>>> 无法获取到新添加的数据 count={}", empEntities.size());

        TrxEmpEntity emp = new TrxEmpEntity();
        emp.setDeptno(deptNo);
        emp.setEname(RandomUtil.username());
        emp.setEmpno(RandomUtils.nextInt(10, 100000));
        emp.setEmpno(51622);
        empMapper.insert(emp);

        empEntities = empMapper.selectList(new LambdaQueryWrapperX<TrxEmpEntity>()
                .between(TrxEmpEntity::getEmpno, 51600, 59031));
        log.info(">>>>>> 新添加的数据被获取成功 count={}", empEntities.size());
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
