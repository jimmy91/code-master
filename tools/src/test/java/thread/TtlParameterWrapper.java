package thread;

import com.alibaba.ttl.TransmittableThreadLocal;

public class TtlParameterWrapper {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    private static final ThreadLocal<String> TRANSMITTABLE_THREAD_LOCAL = new TransmittableThreadLocal<>();

    private TtlParameterWrapper() {
    }

    public static String getCaller(String wrapper) {
        switch (wrapper) {
            case "THREAD_LOCAL":
                return THREAD_LOCAL.get();
            case "INHERITABLE_THREAD_LOCAL":
                return INHERITABLE_THREAD_LOCAL.get();
            case "TRANSMITTABLE_THREAD_LOCAL":
                return TRANSMITTABLE_THREAD_LOCAL.get();
        }
        return "错误wrapper";
    }

    public static void setCaller(String wrapper, String caller) {
        switch (wrapper) {
            case "THREAD_LOCAL":
                THREAD_LOCAL.set(caller);
                break;
            case "INHERITABLE_THREAD_LOCAL":
                INHERITABLE_THREAD_LOCAL.set(caller);
                break;
            case "TRANSMITTABLE_THREAD_LOCAL":
                TRANSMITTABLE_THREAD_LOCAL.set(caller);
                break;
        }
    }

    public static void clear(String wrapper) {
        switch (wrapper) {
            case "THREAD_LOCAL":
                THREAD_LOCAL.remove();
            case "INHERITABLE_THREAD_LOCAL":
                INHERITABLE_THREAD_LOCAL.remove();
            case "TRANSMITTABLE_THREAD_LOCAL":
                TRANSMITTABLE_THREAD_LOCAL.remove();
        }
    }
}
