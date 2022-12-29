package app.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import utils.generator.common.entity.BaseDO;

/**
 * @description: <字典数据表实体>
 * @author: Jimmy
 * @date: 2022-12-29
 * @remark:
 */
@Data
@TableName("system_dict_data")
@ApiModel(value = "字典数据表实体")
public class SystemDictDataDO extends BaseDO {

    private static final long serialVersionUID = 8113240514468936733L;

            @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "字典编码")
    private Long id;

    @ApiModelProperty(value = "字典排序")
    private Integer sort;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典键值")
    private String value;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty(value = "颜色类型")
    private String colorType;

    @ApiModelProperty(value = "css 样式")
    private String cssClass;

    @ApiModelProperty(value = "备注")
    private String remark;

}