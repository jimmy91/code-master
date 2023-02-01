import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class TrackTest {

    public static void main(String[] args) {

        Tracer tracer = GlobalTracer.get();
        // 创建Span。
        Span span = tracer.buildSpan("parentSpan").withTag("myTag", "spanFirst").start();
        tracer.scopeManager().activate(span);
        tracer.activeSpan().setTag("methodName", "testTracing");
        // 业务逻辑。
        // secondBiz();
        span.finish();
        System.out.println("success");
    }
}
