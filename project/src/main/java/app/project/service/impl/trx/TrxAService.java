package app.project.service.impl.trx;

import app.project.entity.trx.TrxDeptEntity;
import app.project.entity.trx.TrxEmpEntity;
import app.project.mapper.trx.TrxDeptMapper;
import app.project.mapper.trx.TrxEmpMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.tools.mybatis.QueryWrapperX;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 事务传播机制的测试类
 * @author Jimmy
 */
@Service
public class TrxAService {

    @Autowired
    private TrxEmpMapper empMapper;
    @Autowired
    private TrxDeptMapper deptMapper;
    @Autowired
    private TrxBService bService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 插入部门,这个方法会同时插入部门和员工
     * 由于无事务处理，添加了一个部门和一个员工，但后续的员工没有添加进去造成了脏数据
     */
    public void insertDept(TrxDeptEntity dept, List<TrxEmpEntity> emps){
        deptMapper.insert(dept);
        // SqlSessionUtils.getSqlSession(sqlSessionFactory).clearCache();
        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true){
                throw new IllegalArgumentException("出 bug 了");
            }
        }
    }

    /**
     * 加上事务后，出异常回滚代码，一个部门和员工都没有添加进去，实现了幂等性
     * @param dept
     * @param emps
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertDeptTransaction(TrxDeptEntity dept, List<TrxEmpEntity> emps){
        deptMapper.insert(dept);

        for (TrxEmpEntity emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 分步操作，调用的方法无事务，但此方法有事务
     * @param dept
     * @param emps
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertSteps(TrxDeptEntity dept, List<TrxEmpEntity> emps){
        deptMapper.insert(dept);

        // insertTrxEmpEntitys 无事务，会加入当前事务
        bService.insertTrxEmpEntitys(emps);
    }

    public void noTransCallTrans(TrxDeptEntity dept,List<TrxEmpEntity> emps){
        deptMapper.insert(dept);

        bService.insertTrxEmpEntitysTransaction(emps);
    }

    /**
     * 测试嵌套事务
     * 可以修改调用方法来达到不同的效果
     * insertTrxEmpEntitysTransactionRequired
     * insertTrxEmpEntitysTransactionRequiredNew
     * insertTrxEmpEntitysTransactionNested
     * insertTrxEmpEntitysTransactionSupport
     * insertTrxEmpEntitysTransactionNotSupport
     * insertTrxEmpEntitysTransactionMandatory
     * insertTrxEmpEntitysTransactionNever
     */
    @Transactional(rollbackFor = Exception.class)
    public void transactionParallel(TrxDeptEntity dept, List<TrxEmpEntity> emps){
        deptMapper.insert(dept);
        bService.insertTrxEmpEntitysTransactionRequired(emps);
        if(true) throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionAsync(TrxDeptEntity dept, List<TrxEmpEntity> emps) throws ExecutionException, InterruptedException {
        deptMapper.insert(dept);

        bService.insertTrxEmpEntitysFuture(emps);

        if(true) throw new IllegalArgumentException("出 bug 了");
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionAsyncInner(TrxDeptEntity dept, List<TrxEmpEntity> emps) throws ExecutionException, InterruptedException {
        deptMapper.insert(dept);

        bService.insertTrxEmpEntitysFutureInner(emps);
    }

    public void trx09(TrxDeptEntity dept, List<TrxEmpEntity> emps) {
        trx09(emps, dept);
    }
    private void trx09(List<TrxEmpEntity> emps, TrxDeptEntity dept) {
        bService.trx09(emps, dept);
    }
    public void trx09(TrxDeptEntity dept){
        deptMapper.insert(dept);
    }


    public void trx10(TrxDeptEntity dept, List<TrxEmpEntity> emps) throws ExecutionException, InterruptedException {
        bService.trx10(emps, dept);
    }
    @Transactional(rollbackFor = Exception.class)
    public void trx10(TrxDeptEntity dept){
        trx10new(dept);
    }
    @Transactional(rollbackFor = Exception.class)
    private void trx10new(TrxDeptEntity dept) {
        TrxDeptEntity deptInfo = deptMapper.selectOne(new QueryWrapperX<TrxDeptEntity>()
                .eq("deptno", dept.getDeptno()));
        deptMapper.insert(dept);

        deptInfo = deptMapper.selectOne(new QueryWrapperX<TrxDeptEntity>()
                .eq("deptno", dept.getDeptno()));
        deptInfo.setLoc("XXX");
        deptMapper.updateById(deptInfo);
    }
}
