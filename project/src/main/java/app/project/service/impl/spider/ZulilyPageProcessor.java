package app.project.service.impl.spider;


import app.project.service.impl.spider.pipeline.FilePipeline;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy
 */
@Slf4j
public class ZulilyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        Map<String, Object> pageParam = page.getRequest().getExtras();
        String path = pageParam.get("path").toString();
        String categoryId = pageParam.get("categoryId").toString();
        // 获取当前页码
        int currentPage = Integer.parseInt(page.getUrl().regex("(.*page=(\\d+))", 2).get());

        // 数据抓取
        Html html = page.getHtml();
        List<String> pids = html.xpath("//*[@id=\"products-list-container\"]/ul/li/@data-product-id").all();
        List<String> eventIds = html.xpath("//*[@id=\"products-list-container\"]/ul/li/@data-eid").all();
        List<String> details = html.xpath("//*/a/@href").all();
        List<String> names = html.xpath("//*/a/div[2]/div[1]/text()").all();
        List<String> imgs = html.xpath("//*/a/div[1]/div[1]/img/@src").all();
        List<String> prices = html.xpath("//*/a/div[2]/div[2]/div[1]/text()").all();
        //List<String> oldPrices = html.xpath("//*/a/div[2]/div[2]/div[2]/text()").all();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            int count = FilePipeline.saveImg(path, imgs.get(i), pids.get(i), String.format("%02d-%d", currentPage, i));
            content.append(StrUtil.join(",", eventIds.get(i), pids.get(i), count, names.get(i).replaceAll(",", "|"), prices.get(i), imgs.get(i), details.get(i)) + "\n");
        }
        if (pids.size() > 50) {
            currentPage++;
            if (currentPage <= 10) {
                // 构造下一页URL
                String nextPageUrl = String.format(CATEGORY_SPIDER_URL, categoryId, currentPage) + URL_PARAM;
                // 添加下一页URL到待爬取队列中
                log.info("添加下一页数据 page={}", currentPage);
                Request req = page.getRequest();
                req.setUrl(nextPageUrl);
                page.addTargetRequest(req);
            }
        }
        FilePipeline.saveToLocalFile(path, content.toString());
        //log.info(content.toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static final String CATEGORY_SPIDER_URL = "https://www.zulily.com/category/products/%s/?page=%s";
    private static final String URL_PARAM = "&lastPos=0&price=0to25%7C25to50";

    public static void main(String[] args) {
        // Kitchen Tools & Utensils： https://www.zulily.com/category/products/143/?page=1&lastPos=0&price=0to25%7C25to50
        // 143  - Kitchen Tools & Utensils  - 厨房工具和用具
        // 157  - Collectibles & Figurines  - 收藏品和小雕像
        // 159  - Holiday Décor - 节日装饰
        // 166  - Outdoor Décor - 室外装饰
        // 22   - Storage & Organization  -  存储和收纳
        // 24   - Cleaning & Laundry - 清洁
        // 29   - Tools & Home Improvement  - 家居工具
        Map<Integer, String> spiderMap = MapUtil.builder(new HashMap<Integer,String>(16))
                //.put(143, "厨房工具和用具")
                //.put(157, "收藏品和小雕像")
                .put(159, "节日装饰")
                //.put(166, "室外装饰")
                //.put(22, "存储和收纳")
                //.put(24, "清洁")
                .put(29, "家居工具")
                .build();
        long start = System.currentTimeMillis();

        spiderMap.forEach((k, v) -> {
            // 清理数据 删除已存在的csv文件
            FilePipeline.localFileCache.remove();
            FilePipeline.deletedCsvFile(v);

            // 设置信息
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryId", k.toString());
            paramMap.put("path", v);
            Request req = new Request();
            req.setUrl(String.format(CATEGORY_SPIDER_URL, k, 1) + URL_PARAM);
            req.setExtras(paramMap);

            log.info("开始【{}】类目的商品下载......", v);
            Spider.create(new ZulilyPageProcessor())
                    //.addUrl("https://www.zulily.com/category/products/143/?page=1&lastPos=0&price=0to25%7C25to50")
                    .addRequest(req)
                    .thread(10).run();
            log.info("完成【{}】类目的商品下载......", v);
        });

        long end = System.currentTimeMillis();
        log.info("完成全部类目的商品下载......耗时： {}秒", (end-start)/1000 );
    }
}