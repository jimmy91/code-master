package utils.generator.common.dao.req;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Jimmy
 * @description: <查询业务类>
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "查询实体类")
public class QueryBO<T> extends PageParam{

    @ApiModelProperty(value = "实体参数", notes = "查询条件")
    private T entity;

    public QueryWrapper initQueryWrapper() {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.setEntity(entity);
        return wrapper;
    }

    public Page<T> getPage(){
        return buildPage(new PageParam(this.getPageNo(), this.getPageNo()));
    }

    public static <T> Page<T> buildPage(PageParam pageParam) {
        if(Objects.isNull(pageParam)){
            pageParam = new PageParam();
        }
        return buildPage(pageParam, null);
    }

    public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        // 排序字段
        if (!CollectionUtil.isEmpty(sortingFields)) {
            page.addOrder(sortingFields.stream().map(sortingField -> SortingField.ORDER_ASC.equals(sortingField.getOrder()) ?
                            OrderItem.asc(sortingField.getField()) : OrderItem.desc(sortingField.getField()))
                    .collect(Collectors.toList()));
        }
        return page;
    }
}