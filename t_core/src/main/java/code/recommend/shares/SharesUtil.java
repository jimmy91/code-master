package code.recommend.shares;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import code.recommend.service.FileDataSource;
import code.recommend.shares.item.SharesCurveItem;
import code.recommend.shares.item.SharesItem;
import code.recommend.shares.strategy.CurveVo;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static code.recommend.shares.SharesConstants.*;

/**
 * @author jimmy
 */
@Slf4j
public class SharesUtil {

    public static String folderPath;

    static {
        folderPath = Objects.requireNonNull(FileDataSource.class.getResource("/shares")).getPath();
    }
    static DecimalFormat decimalFormat = new DecimalFormat("#0.0");
    public static String formatCurve(Double curve){
        if(curve >= 0){
            return String.format(" %s", decimalFormat.format(curve));
        }else{
           return decimalFormat.format(curve);
        }
    }

    public static String sendGet(String url){
        HttpRequest request = HttpRequest.get(url);
        request.header("User-Agent", SharesConstants.USER_AGENT);
        request.header("cookie", SharesConstants.COOKIE);
        HttpResponse response = request.execute();

        //System.out.println("Response Code: " + response.getStatus());
        return response.body();
    }



    /**
     * 方法描述: 读取股票基础数据
     *
     * @Return {@link List<   SharesItem   >}
     * @author jimmy
     */
    public static List<SharesItem> getData() throws IOException {
        List<SharesItem> itemList = Lists.newArrayList();
        if(!FileUtil.exist(folderPath + "\\shares.item")){
            return itemList;
        }
        BufferedReader in = null;
        FileInputStream out = null;
        InputStreamReader reader = null;
        try {
            out = new FileInputStream(folderPath + "\\shares.item");
            reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            in = new BufferedReader(reader);
            String line;
            boolean isNewData = false;
            while ((line = in.readLine()) != null) {
                isNewData = (isNewData || line.contains(DateUtil.formatDate(DateUtil.date())));
                if(!isNewData){
                    break;
                }
                if(line.contains("代码")){
                    continue;
                }
                String[] ht = line.split("[    ]+");
                String code = ht[0];
                String name = ht[1];
                String totalPrice = ht[2];
                String price = ht[3];
                Double limit = Double.parseDouble(ht[4].replace("%", ""));
                Double changeRate = Double.parseDouble(ht[5].replace("%", ""));
                SharesItem dto = new SharesItem(code, name, totalPrice, price, limit, changeRate);
                itemList.add(dto);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            in.close();
            reader.close();
            out.close();
        }
        return itemList;
    }

    public static List<List<SharesCurveItem>> getItemCurve(String name){
        try {
            String serName = Objects.nonNull(name) ? name : DateUtil.formatDate(DateUtil.date());
            if(FileUtil.exist(folderPath + "\\curve.ser."+serName)){
                long start = System.currentTimeMillis();
                log.info("反序列近5日趋势数据开始...");
                // 文件已存在，并且符合时效
                File curveSerIn = FileUtil.file(folderPath + "\\curve.ser."+serName);
                FileInputStream fileIn = new FileInputStream(curveSerIn);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                // 从文件中反序列化对象
                List<List<SharesCurveItem>> items = (List<List<SharesCurveItem>>) in.readObject();

                // 关闭流
                in.close();
                fileIn.close();
                long end = System.currentTimeMillis();
                log.info("反序列近5日趋势数据成功...耗时 {}秒", (end-start)/1000);
                return items;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static List<CurveVo> findFastUp(List<Double> array, int minute, double limit) {
        List<CurveVo> vos = new ArrayList<>();
        List<List<Double>> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (start < array.size()) {
            List<Double> subarray = new ArrayList<>();
            Double min = Double.MAX_VALUE;
            Double max = -20D;
            while (end < array.size() && end - start <= minute) {
                if (array.get(end) > max) {
                    max = array.get(end);
                }
                if (array.get(end) < min) {
                    min = array.get(end);
                    // 最大值必须在后面
                    max = array.get(end);
                    subarray.clear();
                }
                subarray.add(array.get(end));
                if (max - min >= limit) {
                    result.add(new ArrayList<>(subarray));
                    List<String> keywords = new ArrayList<>();
                    CurveVo vo = new CurveVo();
                    vo.setRecords(subarray);
                    vo.setTag(KSSK);
                    // 校验有没有下落，超过一半说明快速跌落，
                    int last = end + minute < array.size() ?  end + minute : array.size() - 1;
                    if( array.get(last) < min ){
                        keywords.add(SharesConstants.LGCH);
                    }else if(array.get(last) < min + limit/2){
                        keywords.add(SharesConstants.ZLFQ);
                    }else if(array.get(last) < max){
                        keywords.add(SharesConstants.GWZD);
                    }else{
                        keywords.add(SharesConstants.KSLS);
                    }
                    vo.setKeywords(keywords);
                    vos.add(vo);
                    break;
                }
                end++;
            }
            start = end + 1;
            end = start;
        }
        //System.out.println(result);
        return vos;
    }


    public static List<CurveVo> findFastDown(List<Double> array, int minute, double limit) {
        List<CurveVo> vos = new ArrayList<>();
        List<List<Double>> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (start < array.size()) {
            List<Double> subarray = new ArrayList<>();
            Double min =  Double.MAX_VALUE;
            Double max = -60D;
            while (end < array.size() && end - start <= minute) {
                if (array.get(end) > max) {
                    max = array.get(end);
                    // 最小值必须在后面
                    min = array.get(end);
                    subarray.clear();
                }
                if (array.get(end) < min) {
                    min = array.get(end);
                }
                subarray.add(array.get(end));
                if (max - min >= limit) {
                    result.add(new ArrayList<>(subarray));

                    CurveVo vo = new CurveVo();
                    vo.setRecords(subarray);
                    List<String> keywords = new ArrayList<>();
                    vo.setTag(KSLD);
                    int last = end + minute < array.size() ?  end + minute : array.size() - 1;
                    if( array.get(last) < min - limit ){
                        keywords.add(LXLD);
                    }else if(array.get(last) < min + limit/2){
                        keywords.add(SharesConstants.DWZD);
                    }else if(array.get(last) < max){
                        keywords.add(SharesConstants.DWBC);
                    }else{
                        keywords.add(SharesConstants.DWLS);
                    }
                    vo.setKeywords(keywords);
                    vos.add(vo);
                    break;
                }
                end++;
            }
            start = end + 1;
            end = start;
        }
        System.out.println(result);
        return vos;
    }


    public static void main(String[] args) {
        List<Double> array = Arrays.asList(3D, 7D, 1D, 2D, 5D, 4D, 8D, 6D, 9D, 7D, 10D, 12D, 15D, 9D, 14D, 15D, 13D, 20D, 18D, 19D, 17D, 16D);
        //List<Double> array = Arrays.asList(9D, 7D);
         for(int i = 0 ; i < array.size(); i++){
             array.set(i, -1* array.get(i));
         }
        findFastUp(array, 10, 5);
        findFastDown(array, 10, 5);


        System.out.println(formatCurve(-1.25));
        System.out.println(formatCurve(1.25));

        List<String> allItems = new ArrayList<>();
        allItems.add("x好的");
        allItems.add("呀或");
        allItems = allItems.stream().filter(p -> p.matches("[^a-zA-Z].*")).collect(Collectors.toList());
        System.out.println(allItems);
    }

}
