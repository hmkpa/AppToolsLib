package android.hmm.lib.menu;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014-2-24
 * Description:  
 */
public class MenuBodyItem {

	private String name;
	private int iconId;
//	private int pageNum;
//	private int position;
	private android.view.View.OnClickListener click;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setClick(android.view.View.OnClickListener click) {
		this.click = click;
	}

	public android.view.View.OnClickListener getClick() {
		return click;
	}

//	public void setPageNum(int pageNum) {
//		this.pageNum = pageNum;
//	}
//
//	public int getPageNum() {
//		return pageNum;
//	}
//
//	public void setPosition(int position) {
//		this.position = position;
//	}
//
//	public int getPosition() {
//		return position;
//	}

}
