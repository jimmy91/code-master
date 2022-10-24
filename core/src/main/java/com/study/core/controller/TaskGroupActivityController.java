package ${cfg.controllerPackage};

import generatorTemplate.common.src.main.java.com.study.common.common.entity.TaskGroupActivityDO;
import generatorTemplate.common.src.main.java.com.study.common.common.entity.bo.QueryBO;
import generatorTemplate.common.src.main.java.com.study.common.ext.service.TaskGroupActivityServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: ${cfg.controllerPackage}
 * @Description: <自由团活动信息表前端控制器>
 * @Author: Jimmy
 * @CreateDate: 2022-10-24
 * @UpdateUser: Jimmy
 * @UpdateDate: 2022-10-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "自由团活动信息表-相关接口")
@RequestMapping(value = "/task/groupActivity", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskGroupActivityController {
    @Autowired
    private TaskGroupActivityServiceExt iTaskGroupActivityServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "自由团活动信息表分页查询")
    public Page<TaskGroupActivityDO> listTaskGroupActivityServiceByPage(QueryBO<TaskGroupActivityDO> query) {
        return iTaskGroupActivityServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "自由团活动信息表获取某一实体")
    public TaskGroupActivityDO getTaskGroupActivityServiceDetails(@PathVariable Long id) {
        return iTaskGroupActivityServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "自由团活动信息表新增数据")
    public boolean saveTaskGroupActivityService(@RequestBody TaskGroupActivityDO dto) {
        return iTaskGroupActivityServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "自由团活动信息表修改数据")
    public boolean modifyTaskGroupActivityService(@RequestBody TaskGroupActivityDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iTaskGroupActivityServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "自由团活动信息表批量删除数据")
    public boolean batchRemoveTaskGroupActivityService(@RequestParam(value = "ids") List<Long> ids) {
        return iTaskGroupActivityServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "自由团活动信息表删除数据")
    public boolean removeTaskGroupActivityService(@PathVariable Long id) {
        return iTaskGroupActivityServiceExt.removeById(id);
    }
}