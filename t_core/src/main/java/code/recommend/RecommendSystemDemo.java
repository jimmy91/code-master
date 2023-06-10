package code.recommend;

import code.recommend.dto.ItemDTO;
import code.recommend.service.Recommend;

import java.util.List;

/**
 * @author jimmy
 * <p>
 * https://gitee.com/taisan/recommend_system/
 */
public class RecommendSystemDemo {

    public static void main(String[] args) {
        //SpringApplication.run(RecommendSystemApplication.class, args);
        System.out.println("------基于用户协同过滤推荐---------------下列电影");
        List<ItemDTO> itemList = Recommend.userCfRecommend(1);
        itemList.forEach(e -> System.out.println(e.getName()));
        System.out.println("------基于物品协同过滤推荐---------------下列电影");
        List<ItemDTO> itemList1 = Recommend.itemCfRecommend(1);
        itemList1.forEach(e -> System.out.println(e.getName()));
    }

}





