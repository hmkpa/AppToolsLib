package android.hmm.lib.menu;

import java.util.List;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-24
 * Description:  
 */
public class MenuTitleItem {

	private String name;
	private int flag;
	private List<MenuBodyItem> bodyList;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}

	public void setBodyList(List<MenuBodyItem> bodyList) {
		this.bodyList = bodyList;
	}

	public List<MenuBodyItem> getBodyList() {
		return bodyList;
	}

}
