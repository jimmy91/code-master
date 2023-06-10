package code.recommend.shares;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import code.recommend.core.CoreMath;
import code.recommend.service.FileDataSource;
import code.recommend.shares.dto.SharesRelateDTO;
import code.recommend.shares.item.SharesCurveItem;
import code.recommend.shares.item.SharesItem;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.thymeleaf.util.ListUtils;
import utils.tools.coll.CollectionUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static code.recommend.shares.SharesUtil.getData;

/**
 * 通过波尔森公式，计算股票之前的相关度，并给出相关度高的最近5日涨幅比对趋势
 * @author jimmy
 */
@Slf4j
public class DownloadSharesCurveDemo {

    public static String folderPath;

    static {
        folderPath = Objects.requireNonNull(FileDataSource.class.getResource("/shares")).getPath();
    }

    static ExecutorService executorService = new ThreadPoolExecutor(10, 30, 60,  TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));




    public static String toCurveData(List<SharesCurveItem> items){
        if(CollUtil.isEmpty(items)){
            return "\n";
        }
        StringBuilder data = new StringBuilder();
        String content = String.format("%s  %-6s  %s到%s", items.get(0).getCode(), items.get(0).getName(), items.get(0).getStrtime(), items.get(items.size()-1).getStrtime());
        data.append(content);
        items.forEach(i -> data.append("   " + i.getRatio()));
        data.append("\n");
        return data.toString();
    }

    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     *
     * @param xs x集合
     * @param ys y集合
     * @Return {@link double}
     * @author jimmy
     */
    static DecimalFormat decimalFormat = new DecimalFormat("#0.0000");

    public static double getRelate(List<Double> xs, List<Double> ys) {
        if(!Objects.equals(xs.size(), ys.size())){
            return 0D;
        }
        double result = CoreMath.getRelate(xs, ys);
        String formattedNumber = decimalFormat.format(result);
        return Double.parseDouble(formattedNumber);
    }


    public static void processTask() throws Exception {

        List<List<SharesCurveItem>> allItems = getItemCurve();
        long startTime = System.currentTimeMillis();
        log.info("计算相关度开始， 共 {}支股票", allItems.size());
        // 相关度计算
        List<SharesRelateDTO> relateDTOS = new ArrayList<>();
        // 排除以字母开头的股票
        allItems = allItems.stream().filter(p -> p.get(0).getName().matches("[^a-zA-Z].*")).collect(Collectors.toList());
        for(int i = 1; i < allItems.size() ; i++){
            if(i%200 == 0){ log.info("计算进行中... i:{} ", i);}
            for(int y = i+1; y < allItems.size() ; y++){
                List<SharesCurveItem> xItems = allItems.get(i);
                List<SharesCurveItem> yItems = allItems.get(y);
                try{
                    List<Double> xs = CollectionUtils.convertList(xItems, SharesCurveItem::getRatio);
                    List<Double> ys = CollectionUtils.convertList(yItems, SharesCurveItem::getRatio);
                    Double relate = getRelate(xs, ys);
                    if(relate >= SharesConstants.RELATE || relate < SharesConstants.UN_RELATE){
                        StringBuffer xRatios = new StringBuffer();
                        StringBuffer yRatios = new StringBuffer();
                        for(int xi = 0; xi < xItems.size(); xi++){
                            if(SharesConstants.NODE_INDEX.contains(xi) || SharesConstants.NODE_INDEX.contains(xi%SharesConstants.DAY_RELATE_NUM)){
                                xRatios.append(SharesUtil.formatCurve(xItems.get(xi).getRatio())).append("-> ");
                            }
                        }
                        for(int yi = 0; yi < yItems.size(); yi++){
                            if(SharesConstants.NODE_INDEX.contains(yi) || SharesConstants.NODE_INDEX.contains(yi%SharesConstants.DAY_RELATE_NUM)){
                                yRatios.append(SharesUtil.formatCurve(yItems.get(yi).getRatio())).append("-> ");
                            }
                        }
                        String code = String.format("%s#%s", xItems.get(0).getCode(), yItems.get(0).getCode());
                        String name = String.format("%s#%s", xItems.get(0).getName(), yItems.get(0).getName());
                        String price = String.format("%s->%s|%s->%s", xItems.get(0).getPrice(), xItems.get(xItems.size()-1).getPrice(),
                                yItems.get(0).getPrice(), yItems.get(yItems.size()-1).getPrice());
                        SharesRelateDTO dto = new SharesRelateDTO(code, name, relate, price, xRatios.toString(), yRatios.toString());
                        relateDTOS.add(dto);
                    }
                }catch (Exception e){
                    log.error("计算股票相关度异常 {}#{} {}#{} e={}", xItems.get(0).getCode(), yItems.get(0).getCode(),
                            xItems.get(0).getName(), yItems.get(0).getName(), e);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("计算相关度完成， 共 {}支股票  相关性 {}对股，耗时 {}秒", allItems.size(), relateDTOS.size(), (endTime-startTime)/1000);
        // 排序
        CollUtil.sort(relateDTOS, Comparator.comparing(SharesRelateDTO::getRelate));
        CollUtil.reverse(relateDTOS);

        FileUtil.del(folderPath + "\\shares.relate");
        File relate = FileUtil.file(folderPath + "\\shares.relate");
        PrintWriter relatePrintWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(relate),"UTF-8"));
        relatePrintWriter.append(String.format("相关代码                相关名称            相关度            趋势(截止时间：%s)\n", DateUtil.formatDateTime(DateUtil.date())));
        relateDTOS.forEach(r -> relatePrintWriter.append(r.toData()));
        relatePrintWriter.close();
        log.info("股票相关度数据文件完成.... 有效数据共 {}条", relateDTOS.size());

        // 文件序列化
        FileUtil.del(folderPath + "\\relate.ser."+ DateUtil.formatDate(DateUtil.date()));
        File relateSer = FileUtil.file(folderPath + "\\relate.ser."+ DateUtil.formatDate(DateUtil.date()));
        FileOutputStream fileOut = new FileOutputStream(relateSer);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        // 将对象序列化并写入文件
        out.writeObject(relateDTOS);
        // 关闭流
        out.close();
        fileOut.close();
        log.info("股票相关度数据文件序列化完成.... 有效数据共 {}条", relateDTOS.size());

    }

    @NotNull
    private static List<List<SharesCurveItem>> getItemCurve() throws IOException, ClassNotFoundException {
        List<List<SharesCurveItem>> localItems = SharesUtil.getItemCurve(null);
        if(Objects.nonNull(localItems)){
            return localItems;
        }

        long startTime = System.currentTimeMillis();
        List<SharesItem> sharesItems = getData();
        List<List<SharesCurveItem>> allItems = new ArrayList<>();
        sharesItems.parallelStream().forEach(p -> {
            List<SharesCurveItem> items = downStaresCurve(p);
            if(Objects.nonNull(items)){
                allItems.add(items);
            }
        });

        long endTime = System.currentTimeMillis();
        log.info("下载数据完成，共计 {} 支股票，耗时 {}秒", allItems.size(), (endTime - startTime)/1000);


        FileUtil.del(folderPath + "\\shares.curve");
        File file = FileUtil.file(folderPath + "\\shares.curve");
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        printWriter.append(String.format("代码        名称        时间       涨幅指数(截止时间：%s)\n", DateUtil.formatDateTime(DateUtil.date())));
        allItems.parallelStream().filter(p -> !ListUtils.isEmpty(p)).forEach(i -> {
            printWriter.append(toCurveData(i));
            //ThreadUtil.sleep(100L);
        });
       printWriter.close();
       log.info("股票近5日趋势数据保存完成....");

        // 文件序列化
        FileUtil.del(folderPath + "\\curve.ser."+ DateUtil.formatDate(DateUtil.date()));
        File curveSer = FileUtil.file(folderPath + "\\curve.ser."+ DateUtil.formatDate(DateUtil.date()));
        FileOutputStream fileOut = new FileOutputStream(curveSer);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        // 将对象序列化并写入文件
        out.writeObject(allItems);
        // 关闭流
        out.close();
        fileOut.close();
        log.info("股票近5日趋势数据文件序列化完成....");

        return allItems;
    }


    /**
     * 获取近5日趋势数据
     * @param p
     * @return
     */
    public static List<SharesCurveItem> downStaresCurve(SharesItem p){
        if(!Objects.equals(p.getCode().length(), 6)){
            return null;
        }
        if(!p.getName().matches("[^a-zA-Z].*")){
            return null;
        }

        List<SharesCurveItem> curveItems = new ArrayList<>();
        String url = String.format(SharesConstants.SYNC_SHARES_5DAY_CURVE, p.getCode());
        String result = SharesUtil.sendGet(url);
        JSON json = JSONUtil.parse(result);
        if(Objects.equals(0, JSONUtil.getByPath(json, "ResultCode", -1))) {
            if (!Objects.equals("[]", JSONUtil.getByPath(json, "Result.fivedays"))) {
                String fivedays = JSONUtil.getByPath(json, "Result.fivedays", "");
                JSONArray fiveDayArr = JSONUtil.parseArray(fivedays);
                log.info("下载 {} 趋势数据完成", p.getCode());

                fiveDayArr.forEach(d -> {
                    // 一天240条数据，一分钟一条数据
                    JSONArray priceinfos = JSONUtil.parseArray(JSONUtil.getByPath( JSONUtil.parse(d), "priceinfos", ""));
                    for(int i = 0; i < priceinfos.size(); i++){
                        Object d1 = priceinfos.get(i);
                        Double ratio = Double.parseDouble(JSONUtil.parse(d1).getByPath("ratio").toString());
                        Double price = Double.parseDouble(JSONUtil.parse(d1).getByPath("price").toString());
                        Double avgPrice = Double.parseDouble(JSONUtil.parse(d1).getByPath("avgPrice").toString());
                        Double volume = Double.parseDouble(JSONUtil.parse(d1).getByPath("volume").toString());
                        Date time =DateUtil.date(Long.valueOf(JSONUtil.parse(d1).getByPath("time").toString()) * 1000);
                        String strtime = JSONUtil.parse(d1).getByPath("strtime").toString();

                        SharesCurveItem item = new SharesCurveItem();
                        item.setCode(p.getCode());
                        item.setName(p.getName());
                        item.setStrtime(strtime);
                        item.setTime(time);
                        item.setPrice(price);
                        item.setRatio(ratio);
                        item.setVolume(volume);
                        item.setAvgPrice(avgPrice);
                        curveItems.add(item);
                    }
                });
            }

            if(CollUtil.isEmpty(curveItems)){
                SharesCurveItem item = new SharesCurveItem();
                item.setCode(p.getCode());
                item.setName(p.getName());
                curveItems.add(item);
            }
        }else{
            log.error("请求数据异常 url: {}， result: {}", url, result);
            return null;
        }

       return curveItems;
    }

    public static void main(String[] args) throws Exception {
/*
        SharesItem item = new SharesItem();
        item.setCode("600636");
        item.setName("名称");
        downStaresCurve(item);
*/

        processTask();
    }
}
