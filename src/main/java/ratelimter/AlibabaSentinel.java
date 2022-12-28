package ratelimter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jimmy
 * https://sentinelguard.io/zh-cn/docs/quick-start.html
 * https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html
 */
public class AlibabaSentinel {
    public static void main(String[] args) throws InterruptedException {
        // 配置规则.
        initFlowRules();

        for (int i = 0 ; i< 50 ; i++){
            String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
            Thread.sleep(100);
            Entry entry = null;
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
/*
            try (Entry e1 = SphU.entry("HelloWorld");){
                System.out.println(time + "：hello world");
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println(time + ":blocked!");
            }
*/


            try {
                // 被保护的逻辑
                entry = SphU.entry("HelloWorld");
                System.out.println(time + "：hello world");
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println(time + ":blocked!");
            } finally {
                if(entry != null){
                    entry.exit();
                }
            }


            if (SphO.entry("HelloWorld")) {
                // 务必保证finally会被执行
                try {
                    System.out.println(time + "：hello world");
                } finally {
                    SphO.exit();
                }
            } else {
                // 资源访问阻止，被限流或被降级
                // 进行相应的处理操作
                System.out.println(time + ":blocked!");
            }


        }
    }

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(5);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
