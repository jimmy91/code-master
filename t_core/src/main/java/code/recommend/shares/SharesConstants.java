package code.recommend.shares;

import java.util.Arrays;
import java.util.List;

/**
 * @author jimmy
 */
public class SharesConstants {

    public static final String KSSK = "快速上涨";
    public static final String LGCH = "拉高出货";
    public static final String ZLFQ = "主力分歧";
    public static final String GWZD = "高位振荡";
    public static final String KSLS = "快速拉升";

    public static final String KSLD = "快速下跌";
    public static final String LXLD = "连续下跌";
    public static final String DWZD = "低拉振荡";
    public static final String DWBC = "低拉补涨";
    public static final String DWLS = "低拉拉升";

    // 百度股市通
    public static final String COOKIE = "BIDUPSID=E700B90CF524753508FDD6E0BBD80F38; PSTM=1655263882; H_WISE_SIDS=107320_110085_131862_188744_194520_194529_196428_197471_197711_199576_204914_205413_208721_209568_210306_210321_212296_212797_212868_213033_213350_214115_214130_214138_214142_214793_215727_216049_216615_216740_216852_216883_216943_217086_217185_218329_218395_218445_218453_218599_218620_219156_219360_219363_219447_219452_219548_219593_219624_219666_219712_219732_219734_219737_219742_219814_219820_219861_219935_219942_219946_219948_220067_220071_220089_220119_220302_220333_220663_220801_221006_221015_221057_221087_221108_221116_221119_221121_221318_221369_221371_221385_221416_221433_221443_221474_221479_221501_8000066_8000131_8000138_8000149_8000158_8000161_8000163_8000173_8000178_8000181_8000185; BDUSS=lkeHBTTUpiZFJvV1hjazV3WVZrOXc5amJhOGM2Ym5ROTRnZWdHckdGeVJrSFJqSVFBQUFBJCQAAAAAAAAAAAEAAABZCn8eMjQzNzIwMDgyAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJEDTWORA01jV3; BDUSS_BFESS=lkeHBTTUpiZFJvV1hjazV3WVZrOXc5amJhOGM2Ym5ROTRnZWdHckdGeVJrSFJqSVFBQUFBJCQAAAAAAAAAAAEAAABZCn8eMjQzNzIwMDgyAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJEDTWORA01jV3; H_WISE_SIDS_BFESS=107320_110085_131862_188744_194520_194529_196428_197471_197711_199576_204914_205413_208721_209568_210306_210321_212296_212797_212868_213033_213350_214115_214130_214138_214142_214793_215727_216049_216615_216740_216852_216883_216943_217086_217185_218329_218395_218445_218453_218599_218620_219156_219360_219363_219447_219452_219548_219593_219624_219666_219712_219732_219734_219737_219742_219814_219820_219861_219935_219942_219946_219948_220067_220071_220089_220119_220302_220333_220663_220801_221006_221015_221057_221087_221108_221116_221119_221121_221318_221369_221371_221385_221416_221433_221443_221474_221479_221501_8000066_8000131_8000138_8000149_8000158_8000161_8000163_8000173_8000178_8000181_8000185; BAIDUID=35BE02825AC78C8A2D27C671E7F2C6D6:FG=1; MCITY=-340%3A; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; delPer=0; ZFY=waDHvvN7fKOVfzulEdsGKdIStNyrsV28h2E73soyFvs:C; BAIDUID_BFESS=35BE02825AC78C8A2D27C671E7F2C6D6:FG=1; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; BA_HECTOR=058k2la480a0818k8h0g2l0q1i806n21m; H_PS_PSSID=38516_36556_38687_38860_38610_38768_38844_38577_38486_38820_38823_38839_38639_38764_26350_22160_38663; PSINO=6; ab_sr=1.0.1_YzNiYzFkZGU3OWJlNzlhOTMzODgzNGQ4MjMzNThlOTQ4OWZkM2UxOTA5NzI5Y2E3YjU2Yjc5M2EyNzU5MjFmZDJiODllZjY1YjViZTY5ZTgwZWQwYzEzNWM1NGU4YmJkZTkyNmViMDRkYmQ0MmYzNmEyYWVkNzcwYTk1NjgyNjAxZTc0NDhmM2JhNGM1MGJiMDNiYzJmNDljM2M4ZjQ4OQ==";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36";

    public static final List<Integer> NODE_INDEX = Arrays.asList(1, 30, 90, 110, 130, 190, 220);
    // 相似度
    public static final Double RELATE = 0.85D;
    public static final Double UN_RELATE = -0.85D;
    public static final Integer DAY_RELATE_NUM = 240 ;

    /**
     * https://gushitong.baidu.com/
     * 获取股票列表：pn-起始数 rn-每页数
     * sort_key=14 按涨幅  sort_key=24 按市值
     */
    public static final String DOWN_SHARES_URL = "https://finance.pae.baidu.com/selfselect/getmarketrank?sort_type=1&sort_key=14&from_mid=1&pn=%s&rn=%s&group=ranklist&type=ab&finClientType=pc";

    /**
     * 获取股票近五日趋势
     * code : 股票代码
     */
    public static final String SYNC_SHARES_5DAY_CURVE = "https://finance.pae.baidu.com/selfselect/getstockquotation?all=1&code=%s&isIndex=false&isBk=false&isBlock=false&isFutures=false&isStock=true&newFormat=1&group=quotation_fiveday_ab&finClientType=pc";

    public static final String SYNC_SHARES_DETAIL = "https://gushitong.baidu.com/opendata?openapi=1&dspName=iphone&tn=tangram&client=app&query=%s&code=%s&word=%s&resource_id=5429&ma_ver=4&finClientType=pc";

}