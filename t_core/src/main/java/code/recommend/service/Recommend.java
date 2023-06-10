package code.recommend.service;

import code.recommend.core.ItemCF;
import code.recommend.core.UserCF;
import code.recommend.dto.ItemDTO;
import code.recommend.dto.RelateDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐服务
 *
 * @author jimmy
 */
public class Recommend {

    /**
     * 方法描述: 猜你喜欢
     *
     * @param userId 用户id
     * @Return {@link List<ItemDTO>}
     * @author jimmy
     */
    public static List<ItemDTO> userCfRecommend(int userId) {
        List<RelateDTO> data = FileDataSource.getData();
        List<Integer> recommendations = UserCF.recommend(userId, data);
        return FileDataSource.getItemData().stream().filter(e -> recommendations.contains(e.getId())).collect(Collectors.toList());
    }


    /**
     * 方法描述: 猜你喜欢
     *
     * @param itemId 物品id
     * @Return {@link List<ItemDTO>}
     * @author jimmy
     */
    public static List<ItemDTO> itemCfRecommend(int itemId) {
        List<RelateDTO> data = FileDataSource.getData();
        List<Integer> recommendations = ItemCF.recommend(itemId, data);
        return FileDataSource.getItemData().stream().filter(e -> recommendations.contains(e.getId())).collect(Collectors.toList());
    }


}
