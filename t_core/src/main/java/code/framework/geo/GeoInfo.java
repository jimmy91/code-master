package code.framework.geo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jimmy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoInfo {

    @ApiModelProperty("城市名称")
    private String key;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    public String toGeo(){
        return this.longitude + " " + latitude;
    }

}
