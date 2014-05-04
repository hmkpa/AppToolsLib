package android.hmm.lib.intent;

import java.util.HashSet;
import java.util.Set;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-4-13
 * Description:  
 */
public class IntentParse {

	

	private String mAction; // action值
	private Uri mData; // uri
	private String mType; // MimeType
	private String mPackage; // 所在包名
	private ComponentName mComponent; // 组件信息
	private int mFlags; // Flag标志位
	private HashSet<String> mCategories; // Category值
	private Bundle mExtras; // 附加值信息
	// ...
	
	/**
		Intent匹配规则
    	匹配种类有如下三种：      
        *  动作(Action)检测
        *  种类(Category)检测
        *  数据(Data & MimeType)检测
        *  
        SDK中说明的具体规则如下：
        * 一个Intent对象既不包含URI，也不包含数据类型 ; 仅当过滤器也不指定任何URIs和数据类型时，才能通过检测；
              否则不能通过。
        * 一个Intent对象包含URI，但不包含数据类型：仅当过滤器也不指定数据类型，同时它们的URI匹配，才能通过检测。
            例如，mailto:和tel:都不指定实际数据。
        * 一个Intent对象包含数据类型，但不包含URI：仅当过滤也只包含数据类型且与Intent相同，才通过检测。
        * 一个Intent对象既包含URI，也包含数据类型（或数据类型能够从URI推断出） ; 数据类型部分，只有与过滤器中之一
             匹配才算通过；URI部分，它的URI要出现在过滤器中，或者它有content:或file: URI，又或者过滤器没有指定URI。
             换句话说，如果它的过滤器仅列出了数据类型，组件假定支持content:和file: 。
	*/
	
	
/*	
	public class IntentFilter implements Parcelable {
		//匹配算法，，按照匹配规则进行
		//Intent与该IntentFilter进行匹配时调用该方法参数表示Intent的相关属性值
		public final int match(String action, String type, String scheme,
		        Uri data, Set<String> categories, String logTag){
			//首先、匹配Action字段
		    if (action != null && !matchAction(action)) {
		        if (Config.LOGV) Log.v(
		            logTag, "No matching action " + action + " for " + this);
		        return NO_MATCH_ACTION;
		    }
			//其次、匹配数据(Uri和MimeType)字段
		    int dataMatch = matchData(type, scheme, data);
		    //...
		    //最后，匹配Category字段值
		    String categoryMatch = matchCategories(categories);
		    //...
		}
		//...

	}
*/	
	
	
	
	
}
