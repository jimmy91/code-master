package base;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jimmy
 * 总结：
 * <p>
 * 在catch中有return的情况下,finally中的内容还是会执行，并且是先执行finally再return。
 * <p>
 * 需要注意的是，如果返回的是一个基本数据类型，则finally中的内容对返回的值没有影响。因为返回的是 finally执行之前生成的一个副本。
 * <p>
 * 当catch和finally都有return时，return的是finally的值。
 */
public class ExceptionFinallyTest {

    public static void main(String[] args) {

        System.out.println("----------返回数值-----------");
        System.out.println("(catch中return语句会被执行) test00 数值：" + test00(0));
        System.out.println("---------------------");
        System.out.println("test01 数值：" + test01(0));
        System.out.println("---------------------");
        System.out.println("test02 数值：" + test02(0));
        System.out.println("---------------------");

        System.out.println("----------返回对象-----------");
        Obj obj = new Obj(0, 0L, "main", DateUtil.beginOfDay(DateUtil.date()));
        System.out.println("test04 数值：" + test04(obj));
        System.out.println("---------------------");
        obj = new Obj(0, 0L, "main", DateUtil.beginOfDay(DateUtil.date()));
        System.out.println("test05 数值：" + test05(obj));
    }


    public static Obj test05(Obj obj) {
        try {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->try");
            System.out.println("方法体~~~");
            throw new Exception("异常");
        } catch (Exception e) {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->catch");
            System.out.println("catch~~~");
            return obj;
        } finally {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->finally");
            System.out.println("finally~~~");
        }
    }

    public static Obj test04(Obj obj) {
        try {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->try");
            System.out.println("方法体~~~");
            throw new Exception("异常");
        } catch (Exception e) {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->catch");
            System.out.println("catch~~~");
            return obj;
        } finally {
            obj.setI(obj.getI() + 1);
            obj.setL(obj.getL() + 1);
            obj.setDate(DateUtil.offsetDay(obj.getDate(), 1));
            obj.setStr(obj.getStr() + "->finally");
            System.out.println("finally~~~");
            return obj;
        }
    }

    public static int test00(int i) {
        try {
            i++;
            System.out.println("方法体~~~");
            throw new Exception("异常");
        } catch (Exception e) {
            i++;
            System.out.println("catch~~~");
            return i++;
        } finally {
            System.out.println("(说明catch return已提前执行，并以副本的方式保存返回结果)finally数值:" + i);
            i++;
            System.out.println("finally~~~");
            // 会进行catch中的return结果覆盖
            return ++i;
        }
    }


    public static int test01(int i) {
        try {
            i++;
            System.out.println("方法体~~~");
            throw new Exception("异常");
        } catch (Exception e) {
            i++;
            System.out.println("catch~~~");
            // 注意此时返回的是i，并不是i++的结果
            return i++;
        } finally {
            System.out.println("(说明catch return已提前执行，并以副本的方式保存返回结果)finally数值:" + i);
            i++;
            System.out.println("finally~~~");
            // 因为这里是值传递，在执行return前，保留了一个i的副本，然后再去执行finally，finall完后，到return的时候，返回的并不是当前的i，而是保留的那个副本
            // return ++i;
        }
    }

    public static int test02(int i) {
        try {
            i++;
            System.out.println("方法体~~~");
            //先执行return再执行finally
            return i++;
        } catch (Exception e) {
            i++;
            System.out.println("catch~~~");
            // 注意此时返回的是i，并不是i++的结果
            return i++;
        } finally {
            System.out.println("(说明catch return已提前执行，并以副本的方式保存返回结果)finally数值:" + i);
            i++;
            System.out.println("finally~~~");
            // 因为这里是值传递，在执行return前，保留了一个i的副本，然后再去执行finally，finall完后，到return的时候，返回的并不是当前的i，而是保留的那个副本
        }
    }


    @ToString
    @Data
    static class Obj {
        int i;
        Long l;
        String str;
        Date date;


        public Obj(int i) {
            this.i = i;
        }

        public Obj(int i, Long l, String str, Date date) {
            this.i = i;
            this.l = l;
            this.str = str;
            this.date = date;
        }


    }

}
