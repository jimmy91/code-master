package utils.algorithm.bloomfilter;

import java.math.BigDecimal;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/** 布隆过滤器-guava实现
 *  ## 布隆过滤器
 * 在大数据量中，判断一条数据是否在这堆大量数据中；
 *
 * 优点：由于存放的不是完整的数据，所以占用的内存很少，而且新增，查询速度够快；
 *
 * 缺点：随着数据的增加，误判率随之增加；无法做到删除数据；
 * 只能判断数据是否一定不存在，而无法判断数据是否一定存在。(理解：过滤结果如果判断数据不存在那么这条数据实际一定不存在，而过滤结果判断数据存在就不一定实际存在了)
 */
public class BloomfilterDemo {

	private static int size = 1000000;//预计要插入多少数据

	private static double fpp = 0.0001;//期望的误判率

	private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);

	public static void main(String[] args) {
		// 插入数据
		for (int i = 0; i < size; i++) {
			bloomFilter.put(i);
		}

		// 实际存在的过滤
		int fppCount1 = 0;
		for (int i = 0; i < 1000000; i++) {
			boolean mightContain = bloomFilter.mightContain(i);
			if (!mightContain) {
				fppCount1++;
				System.out.println("误判了：" + i);
			}
		}

		System.out.println("总共误判了" + fppCount1 + "次");

		// 实际不存在的过滤
		int fppCount2 = 0;
		for (int i = 1000000; i < 2000000; i++) {
			boolean mightContain = bloomFilter.mightContain(i);
			if (mightContain) {
				fppCount2++;
			}
		}
		BigDecimal f = new BigDecimal(fppCount2);
		BigDecimal s = new BigDecimal(size);

		System.out.println("总共误判了" + fppCount2 + "次，ffp:" + f.divide(s).toString());

	}
}
