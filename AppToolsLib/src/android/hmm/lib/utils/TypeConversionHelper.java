package android.hmm.lib.utils;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-12
 * Description:  
 */
public class TypeConversionHelper {

	/************************************************************
	 * 该类能在 int 类型和 String 类型之间互相转换。
		Integer 是int的一个包装类，内部包含一个int型的数据。
		 
		Integer的使用方法：
		１、属性。
		static int MAX_VALUE：返回最大的整型数；
		static int MIN_VALUE：返回最小的整型数；
		static Class TYPE ：返回当前类型。      
		 
		2、构造函数。 
		Integer(int value) ：通过一个int的类型构造对象；
		Integer(String s) ：通过一个String的类型构造对象；   
		 
		３、方法。   
		1. byteValue()：取得用byte类型表示的整数；
	    2. int compareTo(Integer anotherInteger) ：比较两个整数。相等时返回０;小于时返回负数;大于时返回正数。
		3. int compareTo(Object o) ：将该整数与其他类进行比较。
		4. static Integer decode(String nm) ：将字符串转换为整数。 
		5. double doubleValue() ：取得该整数的双精度表示。
		6. boolean equals(Object obj) ：比较两个对象。 
		7. float floatValue() ：取得该整数的浮点数表示。 
		8. static Integer getInteger(String nm) ：根据指定名确定系统特征值。
		9. static Integer getInteger(String nm, int val) ：上面的重载。 
		10. static Integer getInteger(String nm, Integer val) ：上面的重载。 
		11. int hashCode() ：返回该整数类型的哈希表码。
		12. int intValue() ： 返回该整型数所表示的整数。 
	    13. long longValue() ：返回该整型数所表示的长整数。  
		14. static int parseInt(String s) ：将字符串转换成整数。s必须是十进制数组成，否则抛出NumberFormatException异常。 
		15. static int parseInt(String s, int radix) ：以radix为基数radix返回s的十进制数。
		16. short shortValue() ：返回该整型数所表示的短整数。 
		17. static String toBinaryString(int i) ：将整数转为二进制数的字符串。  
		18. static String toHexString(int i) ：将整数转为十六进制数的字符串。 
		19. static String toOctalString(int i) ：将整数转为八进制数的字符串。
		20. String toString() ：将该整数类型转换为字符串。 
		21. static String toString(int i) ：将该整数类型转换为字符串。不同的是，此为类方法。  
		22. static String toString(int i, int radix) ：将整数i以基数radix的形式转换成字符串。  
		23. static Integer valueOf(String s) ：将字符串转换成整数类型。                     
		24. static Integer valueOf(String s, int radix) ：将字符串以基数radix的要求转换成整数类型。
		
		
		int a=Integer.decode("0144"); //八进制转换结果为100
		int b=Integer.decode("123");//十进制转换结果为123
		int c=Integer.decode("0x123");//16进制转换结果为291
		注：Integer.decode([String])加负号也可以转换,不过字符串不能有空格。否则报NumberFormatException异常
		注: 字串转成 Double, Float, Long 的方法大同小异.
		
		 不常用的：
		1.Integer.toBinaryString(int i) :以二进制无符号整数形式返回一个整数参数的字符串表示形式。
		2.Integer.toHexString(int i) :以十六进制无符号整数形式返回一个整数参数的字符串表示形式。
		3.Integer.toOctalString(int i):以八进制无符号整数形式返回一个整数参数的字符串表示形式。

	 ************************************************************/
	public static int object2Int(Object objcet) {
		/**
			object类型转换为int类型：
			1.如果object是byte,short,int,char类型生成的，那么不用转换直接赋值就ok了。
			2.如果object是字符串类型生成的，先把object转换为String类型的，再把String类型转换为int类型。
			 
			例如.
			String myInt="123";
			Object os=myInt;
			 
			int b=Integer.parseInt((String)os);//还可以os.toString()
			3.如果object是float,double,long类型生成的，思路和上面一样，
			先把object转换为相应的数据类型，然后再转换为int类型。
		 */
		Integer result = null;
		return result;
	}

	/**
	 * String转其他类型类似
	 * @param str
	 * @return
	 */
	public static int string2Int(String str) {
		/**
		 * 如何将String转换成  BigDecimal  ?
		 * BigDecimal d_id = new BigDecimal(str);
		 */
		int result = 0;
		try {
			result = Integer.valueOf(str).intValue();
			result = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 其他类型转String类似
	 */
	public static String int2String(int value) {
		String result = null;
		result = String.valueOf(value);
		result = Integer.toString(value);
		return result;
	}
	
	
	/************************************************************
	 * 	char[]转String有几种方式
	 * 
		 将字符数组转成字符串,我之前记得的是有三种方法
		 第一种://使用构造函数，将字符数组传到函数的实参部分，完成转换
		        String(char[] value) ;//将整个字符数组转为字符串
		        String(char[] value, int offset, int count) ;//将字符数组中的部分转为字符串
		 
		第二种://使用静态方法
		        static String copyValueOf(char[] value) ;
		        static String copyValueOf(char[] value, int offset, int count) ;
		 
		第三种://依旧是静态方法.这个函数和copyValueOf是差不多的,但是这个函数有重载，可以接收int等基本数据
			   //比如: valueOf(int x) --> valueOf(3) ;也是可以返回一个字符串的
		        static String valueOf(char[] value) ;
		 
		第四种: 就是上面哥们说的Arrays.toString()方法了.
		         String str = Arrays.toString(new char[]{'a', '3', 'b'}) ;//返回一个"a3b"的字符串
		         
		第五种: 使用StringBuilder的toString()功能,返回一个字符串.不过这个办法不是严格意义上的将字符数组转成字符串了.
	 * 
	 ************************************************************/
	
	/************************************************************
	 * 
	 ************************************************************/
	/************************************************************
	 * 
	 ************************************************************/

}
