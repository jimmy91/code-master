package code.recommend.shares;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import code.recommend.service.FileDataSource;
import code.recommend.shares.item.SharesItem;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jimmy
 * 通过条件获取全量股票
 */
@Slf4j
public class DownloadSharesDemo {

    public static String folderPath;

    static {
        folderPath = Objects.requireNonNull(FileDataSource.class.getResource("/shares")).getPath();
    }

    /**
     * 总价值范围
     */
    public static Double[] TOTAL_PRICE = {30.0D, 99999.0D};
    /**
     * 单价范围
     */
    public static Double[] PRICE = {3.0D, 9999.0D};

    public static List<SharesItem> downSharesItem() throws IOException {
        // 下载市场所有股票
        int begin = 0;
        int size = 200;
        long startTime = System.currentTimeMillis();
        List<SharesItem> items = new ArrayList<>();
        while (true){
            String result = SharesUtil.sendGet(String.format(SharesConstants.DOWN_SHARES_URL, begin, size));
            JSON json = JSONUtil.parse(result);
            if(Objects.equals(0, JSONUtil.getByPath(json, "ResultCode", -1))){
                if(Objects.equals("[]", JSONUtil.getByPath(json, "Result.Result"))){
                    break;
                }
                log.info("开始下载第{}条数据...", begin);
                String rankStr = JSONUtil.getByPath(json, "Result.Result[0].DisplayData.resultData.tplData.result.rank", "");
                if(Objects.nonNull(rankStr) && !Objects.equals("", rankStr)){
                    try {
                        JSONArray ranks = JSONUtil.parseArray(rankStr);
                        AtomicBoolean hasNew = new AtomicBoolean(false);
                        ranks.forEach(r -> {
                            JSONObject obj = JSONUtil.parseObj(r);
                            String code = JSONUtil.getByPath(obj, "code", "");
                            String name = JSONUtil.getByPath(obj, "name", "").replaceAll(" ", "");

                            // 0 -> 最新价 、 1  -> 涨跌幅  2 -> 换手率  3 -> 成交额  4 -> 成交量  5 -> 振幅  6 -> 总市值
                            JSONArray lst = JSONUtil.parseArray(JSONUtil.getByPath(obj, "list", "")) ;
                            String price =  JSONUtil.parseObj(lst.get(0)).getStr("value");
                            Double limit =  Double.parseDouble(JSONUtil.parseObj(lst.get(1)).getStr("value").replace("%", ""));
                            Double changeRate = Double.parseDouble(JSONUtil.parseObj(lst.get(2)).getStr("value").replace("%", ""));
                            String totalPrice = JSONUtil.parseObj(lst.get(6)).getStr("value");

                            // 市值不过亿
                            if(!totalPrice.endsWith("万") && !totalPrice.contains("-")){
                                Double tPrice = totalPrice.endsWith("万亿") ? 10000 * Double.parseDouble(totalPrice.replace("万亿", ""))
                                        : Double.parseDouble(totalPrice.replace("亿", ""));
                                // 条件过滤
                                if(Double.parseDouble(price) >= PRICE[0] && Double.parseDouble(price) <= PRICE[1]
                                        && tPrice >= TOTAL_PRICE[0] && tPrice <= TOTAL_PRICE[1] ){
                                    SharesItem item = new SharesItem(code, name, totalPrice, price, limit, changeRate);
                                    items.add(item);
                                    hasNew.set(true);
                                }
                            }

                        });
                        begin += size;
                        if(!hasNew.get()){
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    break;
                }
            }
        }
        if(!CollUtil.isEmpty(items)){
            long endTime = System.currentTimeMillis();
            log.info("下载数据完成，共计 {} 支股票，耗时 {}秒", items.size(), (endTime - startTime)/1000);
            StringBuilder data = new StringBuilder();
            data.append(String.format("代码        名称    总市值    最新价    涨跌幅    换手率(截止时间：%s)\n", DateUtil.formatDateTime(DateUtil.date())));
            items.forEach(i -> data.append(i.toData()));

            FileUtil.del(folderPath + "\\shares.item");
            File file = FileUtil.file(folderPath + "\\shares.item");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
            printWriter.append(data.toString());
            printWriter.close();

        }

        return items;

    }

}
