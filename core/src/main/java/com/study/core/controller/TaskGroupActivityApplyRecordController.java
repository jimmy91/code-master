package ${cfg.controllerPackage};

import generatorTemplate.common.src.main.java.com.study.common.common.entity.TaskGroupActivityApplyRecordDO;
import generatorTemplate.common.src.main.java.com.study.common.common.entity.bo.QueryBO;
import generatorTemplate.common.src.main.java.com.study.common.ext.service.TaskGroupActivityApplyRecordServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: ${cfg.controllerPackage}
 * @Description: <自由团活动报名记录表前端控制器>
 * @Author: Jimmy
 * @CreateDate: 2022-10-24
 * @UpdateUser: Jimmy
 * @UpdateDate: 2022-10-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "自由团活动报名记录表-相关接口")
@RequestMapping(value = "/task/groupActivityApplyRecord", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskGroupActivityApplyRecordController {
    @Autowired
    private TaskGroupActivityApplyRecordServiceExt iTaskGroupActivityApplyRecordServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "自由团活动报名记录表分页查询")
    public Page<TaskGroupActivityApplyRecordDO> listTaskGroupActivityApplyRecordServiceByPage(QueryBO<TaskGroupActivityApplyRecordDO> query) {
        return iTaskGroupActivityApplyRecordServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "自由团活动报名记录表获取某一实体")
    public TaskGroupActivityApplyRecordDO getTaskGroupActivityApplyRecordServiceDetails(@PathVariable Long id) {
        return iTaskGroupActivityApplyRecordServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "自由团活动报名记录表新增数据")
    public boolean saveTaskGroupActivityApplyRecordService(@RequestBody TaskGroupActivityApplyRecordDO dto) {
        return iTaskGroupActivityApplyRecordServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "自由团活动报名记录表修改数据")
    public boolean modifyTaskGroupActivityApplyRecordService(@RequestBody TaskGroupActivityApplyRecordDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iTaskGroupActivityApplyRecordServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "自由团活动报名记录表批量删除数据")
    public boolean batchRemoveTaskGroupActivityApplyRecordService(@RequestParam(value = "ids") List<Long> ids) {
        return iTaskGroupActivityApplyRecordServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "自由团活动报名记录表删除数据")
    public boolean removeTaskGroupActivityApplyRecordService(@PathVariable Long id) {
        return iTaskGroupActivityApplyRecordServiceExt.removeById(id);
    }
}