package code.recommend.shares;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import code.recommend.service.FileDataSource;
import code.recommend.shares.item.SharesCurveItem;
import code.recommend.shares.item.SharesDayData;
import code.recommend.shares.item.SharesItem;
import code.recommend.shares.strategy.ChooseVo;
import code.recommend.shares.strategy.StrategyEnum;
import code.recommend.shares.strategy.StrategyVo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author jimmy
 * 通过条件获取全量股票
 */
@Slf4j
public class ChooseSharesDemo {

    public static String folderPath;

    static {
        folderPath = Objects.requireNonNull(FileDataSource.class.getResource("/shares")).getPath();
    }

    public static void main(String[] args) throws Exception {
        List<SharesItem> items = SharesUtil.getData();
        if(CollUtil.isEmpty(items)){
            items = DownloadSharesDemo.downSharesItem();
        }

        List<List<SharesCurveItem>> localItems = SharesUtil.getItemCurve(null);
        Map<String, List<SharesCurveItem>> shareCurveMap = new HashMap<>();
        localItems.parallelStream().forEach(p -> {
            shareCurveMap.put(p.get(0).getCode(), p);
        });
        localItems.clear();

        List<ChooseVo> chooseVos = new ArrayList<>();
        if(Objects.nonNull(items)){
            items.parallelStream().filter(p -> {
                if(p.getName().matches("[^a-zA-Z].*")){
                    return true;
                }
                // 换手4%-10%
                if(p.getLimit() >= 2D && p.getLimit() <= 5D && p.getChangeRate() > 4.0D && p.getChangeRate() < 10.0D){
                    return true;
                }
                return false;
            }).forEach(p -> {
                try {
                    //if(Objects.equals("688097", p.getCode())){
                    SharesDayData dayData = getItemData(p.getCode());
                    if(Objects.nonNull(dayData)){
                        List<StrategyVo> es = StrategyEnum.doOperation(dayData, shareCurveMap.get(p.getCode()), p);
                        if(CollUtil.isNotEmpty(es)){
                            log.info("符合选股 code={} es={}", p.getCode(), es);
                            ChooseVo chooseVo = new ChooseVo();
                            chooseVo.setSharesItem(p);
                            es.stream().forEach(p1 -> {
                                if(Objects.equals(SharesConstants.KSSK, p1.getTag())){
                                    chooseVo.setUpVos(p1);
                                }else if(Objects.equals(SharesConstants.KSLD, p1.getTag())){
                                    chooseVo.setDownVos(p1);
                                }
                            });
                            chooseVos.add(chooseVo);
                        }
                    }
                    // }
                }catch (Exception e){

                }
            });
        }

        FileUtil.del(folderPath + "\\shares.choose");
        File file = FileUtil.file(folderPath + "\\shares.choose");
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        printWriter.append(String.format("代码        名称        涨幅       现价       关键词(截止时间：%s)\n", DateUtil.formatDateTime(DateUtil.date())));
        Collections.sort(chooseVos, Comparator.comparingInt(o -> o.getUpVos().getKeywords().size()));
        Collections.reverse(chooseVos);
        chooseVos.parallelStream().forEach(i -> {
            printWriter.append(i.toData());
        });
        printWriter.close();
        log.info("股票近5日趋势数据保存完成....");

    }


    public static SharesDayData getItemData(String code){
        String url = String.format(SharesConstants.SYNC_SHARES_DETAIL, code, code, code);
        String result = null;
        try {
            result = SharesUtil.sendGet(url);
            JSON json = JSONUtil.parse(result);
            if(Objects.equals(0, JSONUtil.getByPath(json, "ResultCode", -1))) {
                String list = JSONUtil.getByPath(json, "Result[1].DisplayData.resultData.tplData.result.minute_data.pankouinfos.list").toString();
                if (!Objects.equals("[]", list)) {
                    JSONArray lstArr = JSONUtil.parseArray(list);
                    log.info("下载 {} 详情数据", code);
                    Map<String, Object> paramMap = new HashMap<>();
                    lstArr.forEach(d -> {
                        paramMap.put(JSONUtil.getByPath(JSONUtil.parse(d.toString()),"ename", ""), JSONUtil.getByPath(JSONUtil.parse(d.toString()),"value", ""));
                    });
                    SharesDayData data = JSONUtil.toBean(JSONUtil.toJsonStr(paramMap), SharesDayData.class);
                    data.setCode(code);
                    return data;
                }
            }else{
                log.error("请求数据异常 url: {}， result: {}", url, result);

            }
            return null;
        }catch (Exception e){
            log.error("下载异常 url: {} result:{} e={}", url, result, e);
            return null;
        }

    }



}
