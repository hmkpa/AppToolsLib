package android.hmm.lib.utils;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-10-18
 * Description:  
 */
public class Algorithms {

	/**
	 * 折半算法
	 * @param a
	 * @param start
	 * @param len
	 * @param key
	 * @return key在a中的位置
	 */
	public static int binarySearch(int[] a, int start, int len, int key) {
		int high = start + len, low = start - 1, guess;
		while (high - low > 1) {
			guess = (high + low) / 2;
			if (a[guess] < key) low = guess;
			else high = guess;
		}
		if (high == start + len) return ~(start + len);
		else if (a[high] == key) return high;
		else return ~high;
	}
}
