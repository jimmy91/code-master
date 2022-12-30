package utils.generator.common.dao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@ApiModel("分页参数")
@Data
public class PageParam implements Serializable {

    private static final Integer PAGE_NO = 1;
    private static final Integer PAGE_SIZE = 999;

    @ApiModelProperty(value = "页码，从 1 开始", example = "1")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNo = PAGE_NO;

    @ApiModelProperty(value = "每页条数，最大值为 999", example = "10")
    @Min(value = 1, message = "页码最小值为 1")
    @Max(value = 999, message = "页码最大值为 999")
    private Integer pageSize = PAGE_SIZE;

}
