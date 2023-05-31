package app.project.service.impl.spider;


import app.project.service.impl.spider.constant.Constants;
import app.project.service.impl.spider.pipeline.FilePipeline;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jimmy
 */
@Slf4j
public class ZulilyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);


    @Override
    public void process(Page page) {
        Map<String, Object> pageParam = page.getRequest().getExtras();
        String pageType = pageParam.get(Constants.PAGE_TYPE).toString();

        if(Constants.PAGE_LIST.equals(pageType)){
            downloadPageList(page);
        }else{
            downloadPageDetail(page);
        }

    }



    public static StringBuilder detailContent = new StringBuilder();
    private static void downloadPageDetail(Page page) {
        Map<String, Object> pageParam = page.getRequest().getExtras();
        String pid = pageParam.get(Constants.PID).toString();

        // 数据抓取
        Html html = page.getHtml();

        String title = html.xpath("//*[@id=\"product-info\"]/div/div[1]/h1/text()").toString();
        String price = html.xpath("//*[@id=\"product-price\"]/div/span/span/text()").toString();
        String detail = html.xpath("//*[@id=\"product-description\"]/div[1]/p/text()").toString();

        String width = html.xpath("//*[@id=\"product-description\"]/div[1]/ul/li[2]/span[1]/text()").toString();
        String height = html.xpath("//*[@id=\"product-description\"]/div[1]/ul/li[2]/span[3]/text()").toString();
        String depth = html.xpath("//*[@id=\"product-description\"]/div[1]/ul/li[2]/span[5]/text()").toString();

        // 材料
        String materialContent = html.xpath("//*[@id=\"product-description\"]/div[1]/ul/li[3]/span/text()").toString();

        List<String> pics = html.xpath("//*[@id=\"MagicToolboxSelectorsContainer\"]/ul/li/a/@href").all();
        if(CollUtil.isEmpty(pics)){
            pics = html.xpath("//*[@id=\"MagicZoomPlusImagemagictoolbox1\"]/div[1]/div[2]/img/@src").all();
        }

        String path = Constants.ROOT_PATH + Constants.COPY + "//" + pid + "//";
        AtomicInteger index = new AtomicInteger(1);
        pics.forEach(p -> {
            String np = p.replace("1000x1201", "");
            HttpUtil.downloadFile(np, path + String.format("%s-%s.jpg", pid, index.getAndIncrement()));
        });
        detailContent.append(StrUtil.join(",",page.getUrl(),"Ariza", title, "---", "Parent", "---", "One-Size", detail,
                "颜色", "Neutral", "Adult  13+", 2000,
                "价格", "价格", price, "Hand wash", "CN", 1,
                title, "FALSE", materialContent, "FALSE", " |||||| ",
                width, depth, height, "in", "重量", "kg", " |||||| ",
                height, depth, width, "重量", "kg", "\n"
        ));
        log.info("{} , {}", title, pics);

    }

    private static void downloadPageList(Page page) {
        Map<String, Object> pageParam = page.getRequest().getExtras();
        String category = pageParam.get("path").toString();
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
            // 是否复制产品
            FilePipeline.copyProduct(page, category, imgs.get(i), pids.get(i), details.get(i));
            if(!Constants.ONLY_DOWNLOAD_DETAIL){
                int count = FilePipeline.saveImg(category, imgs.get(i), pids.get(i), prices.get(i), String.format("%02d-%d", currentPage, i), names.get(i).contains(Constants.OWNER));
                content.append(StrUtil.join(",", eventIds.get(i), pids.get(i), count, names.get(i).replaceAll(",", "|"), prices.get(i), imgs.get(i), details.get(i)) + "\n");
            }
        }
        if (pids.size() > 50) {
            currentPage++;
            if (currentPage <= 10) {
                // 构造下一页URL
                String nextPageUrl = String.format(CATEGORY_SPIDER_URL, categoryId, currentPage) + URL_PARAM;
                // 添加下一页URL到待爬取队列中
                log.info("添加下一页数据 category={} page={}", category, currentPage);
                Request req = page.getRequest();
                req.setUrl(nextPageUrl);
                page.addTargetRequest(req);
            }
        }
        if(!Constants.ONLY_DOWNLOAD_DETAIL) {
            FilePipeline.saveToLocalFile(category, content.toString());
        }
        //log.info(content.toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static final String CATEGORY_SPIDER_URL = "https://www.zulily.com/category/products/%s/?page=%s";
    private static final String URL_PARAM = "&lastPos=0&price=0to25%7C25to50";

    private static void run(){
        // Kitchen Tools & Utensils： https://www.zulily.com/category/products/143/?page=1&lastPos=0&price=0to25%7C25to50
        // 143  - Kitchen Tools & Utensils  - 厨房工具和用具
        // 157  - Collectibles & Figurines  - 收藏品和小雕像
        // 159  - Holiday Décor - 节日装饰
        // 166  - Outdoor Décor - 室外装饰
        // 22   - Storage & Organization  -  存储和收纳
        // 24   - Cleaning & Laundry - 清洁
        // 29   - Tools & Home Improvement  - 家居工具
        // 191  - Outdoor Lighting  - 户外灯
        Map<Integer, String> spiderMap = MapUtil.builder(new HashMap<Integer,String>(16))
                .put(143, "厨房工具和用具")
                .put(157, "收藏品和小雕像")
                .put(159, "节日装饰")
                .put(166, "室外装饰")
                .put(22, "存储和收纳")
                .put(24, "清洁")
                .put(29, "家居工具")
                .put(191, "户外灯")
                .build();
        long start = System.currentTimeMillis();

        File ownerFile = new File(Constants.ROOT_PATH + Constants.OWNER);
        if(!ownerFile.exists()){
            ownerFile.mkdir();
        }

        spiderMap.forEach((k, v) -> {
            // 清理数据 删除已存在的csv文件
            FilePipeline.localFileCache.remove();
            FilePipeline.deletedCsvFile(v);

            // 是否仅下载详情数据
            Constants.ONLY_DOWNLOAD_DETAIL = Boolean.FALSE;

            // 设置信息
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryId", k.toString());
            paramMap.put("path", v);
            paramMap.put(Constants.PAGE_TYPE, Constants.PAGE_LIST);
            Request req = new Request();
            req.setUrl(String.format(CATEGORY_SPIDER_URL, k, 1) + URL_PARAM);
            req.setExtras(paramMap);

            log.info("开始【{}】类目的商品下载......", v);
            Spider.create(new ZulilyPageProcessor())
                    //.addUrl("https://www.zulily.com/category/products/143/?page=1&lastPos=0&price=0to25%7C25to50")
                    .addRequest(req)
                    .thread(10).run();
            log.info("完成【{}】类目的商品下载......", v);

            FilePipeline.allDetailUrl.forEach((key, value) -> {
                log.info("下载详情中 detail page={}", value);
                Request detailReq =  new Request();
                paramMap.put(Constants.PID, key);
                paramMap.put(Constants.PAGE_TYPE, Constants.PAGE_DETAIL);
                detailReq.setUrl(value);
                detailReq.setExtras(paramMap);
                Spider.create(new ZulilyPageProcessor())
                        .addRequest(detailReq)
                        .thread(5).run();
            });

            FilePipeline.allDetailUrl.clear();

        });

        FilePipeline.saveToDetailFile(Constants.COPY, detailContent.toString());

        long end = System.currentTimeMillis();
        log.info("完成全部类目的商品下载......耗时： {}秒", (end-start)/1000 );
    }

    public static void main(String[] args) {
        run();
        // 创建一个 ScheduledExecutorService 实例
        /* ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        // 计算距离明天的时间差（以秒为单位）
        long initialDelay = DateUtil.between(DateUtil.date(), DateUtil.endOfDay(DateUtil.date()), DateUnit.SECOND);
        long period = 24 * 60 * 60; // 24 小时

        // 执行任务
        executorService.scheduleAtFixedRate(() -> {
            // 在这里写下你要执行的任务逻辑
            System.out.println("任务执行时间：" + LocalTime.now());
            try{
                run();
            }catch (Exception e){
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.SECONDS);*/
        System.out.println("设置成功");
    }


}