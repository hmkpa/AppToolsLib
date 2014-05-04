package android.hmm.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hmm.lib.R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.app.Activity;

public class KeyboardView extends LinearLayout {
	private Context context;
	private LinearLayout tabLayout;
	private LinearLayout mainLayout;
	private EditText target;
	private int targetID;
	private int textColor;
	private int tabBackground;
	private int btnBackground;
	private View view1;
	private View view2;
	private View view3;
	private String[] normalKey = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "0" };
	private String[] specilKey = { ".", ",", "!", "@", "#", "$", "%", "?", "&", "*", "_", "+", "-", "=", "(", ")", "{", "}", "[", "]", ":", "\"", "|", ";", "'", "\\", "<", ">", "^", "/", "`", "~" };

	public KeyboardView(Context context) {
		super(context);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.createTabButton();
		this.setBackgroundColor(Color.BLUE);
	}

	public KeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardView);
		targetID = typeArray.getResourceId(R.styleable.KeyboardView_target, 0);
		textColor = typeArray.getColor(R.styleable.KeyboardView_textColor, Color.WHITE);
//		tabBackground = typeArray.getResourceId(R.styleable.KeyboardView_tabBackground, R.drawable.button_select_style_2);
//		btnBackground = typeArray.getResourceId(R.styleable.KeyboardView_btnBackground, R.drawable.keyonselect);
		this.createTabButton();
		this.createTableLayout(Tag.KEY_LOWERCASE);
	}

	private void createTabButton() {
		tabLayout = new LinearLayout(context);
		tabLayout.setPadding(0, 0, 0, 10);
		view1 = createTabButton("小写字母", Tag.KEY_LOWERCASE);
		view2 = createTabButton("大写字母", Tag.KEY_UPPERCASE);
		view3 = createTabButton("特殊字符", Tag.KEY_SPECILKEY);
		tabLayout.addView(view1);
		tabLayout.addView(view2);
		tabLayout.addView(view3);
		this.addView(tabLayout);
	}

	private void createTableLayout(int tag) {
		if (mainLayout == null) {
			mainLayout = new LinearLayout(context);
			this.addView(mainLayout);
		} else {
			mainLayout.removeAllViews();
		}
		TableLayout tableLayout = null;
		if (tag == Tag.KEY_UPPERCASE) {
			tableLayout = createTableLayout(normalKey, true);
		}
		if (tag == Tag.KEY_LOWERCASE) {
			tableLayout = createTableLayout(normalKey, false);
		}
		if (tag == Tag.KEY_SPECILKEY) {
			tableLayout = createTableLayout(specilKey, true);
		}
		mainLayout.addView(tableLayout);
	}

	private TableLayout createTableLayout(String[] args, boolean uppercase) {
		TableLayout tableLayout = new TableLayout(context);
		TableRow tbRow = new TableRow(context);
		tableLayout.addView(tbRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		Button btnObj;
		for (String key : args) {
			btnObj = createButton(uppercase ? key : key.toLowerCase(), Tag.KEY_NORMALKEY);
			tbRow.addView(btnObj);
			if (tbRow.getChildCount() >= 6) {
				tbRow = new TableRow(context);
				tableLayout.addView(tbRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
		}
		TableRow.LayoutParams tlp = new TableRow.LayoutParams();
		tlp.span = 2;
		btnObj = createButton("退格", Tag.KEY_BACKSPACE, 100);
		btnObj.setLayoutParams(tlp);
		tbRow.addView(btnObj);
		btnObj = createButton("清除", Tag.KEY_CLEARUP, 100);
		btnObj.setLayoutParams(tlp);
		tbRow.addView(btnObj);
		tableLayout.setShrinkAllColumns(true);
		return tableLayout;
	}

	private Button createTabButton(String text, int tag) {
		Button btnObj = createButton(text, tag, 120);
		btnObj.setTextSize(24);
		btnObj.setHeight(50);
		btnObj.setBackgroundResource(tabBackground);
		btnObj.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
//					v.setBackgroundResource(R.drawable.keyonselect);
					return;
				}
				// v.setBackgroundResource(R.drawable.key_onfocused);
				int tag = (Integer) v.getTag();
				if (view1 != null && view2 != null && view3 != null) {
					view1.setBackgroundResource(tabBackground);
					view2.setBackgroundResource(tabBackground);
					view3.setBackgroundResource(tabBackground);
				}
				switch (tag) {
				case Tag.KEY_UPPERCASE:
					if (view2 != null) {
//						view2.setBackgroundResource(R.drawable.button_select_style_1);
					}
					createTableLayout(Tag.KEY_UPPERCASE);
					break;
				case Tag.KEY_LOWERCASE:
					if (view1 != null) {
//						view1.setBackgroundResource(R.drawable.button_select_style_1);
					}
					createTableLayout(Tag.KEY_LOWERCASE);
					break;
				case Tag.KEY_SPECILKEY:
					if (view3 != null) {
//						view3.setBackgroundResource(R.drawable.button_select_style_1);
					}
					createTableLayout(Tag.KEY_SPECILKEY);
					break;
				}
			}
		});
		return btnObj;
	}

	private Button createButton(String text, Object tag) {
		return createButton(text, tag, 60);
	}

	private Button createButton(String text, Object tag, int width) {
		Button btnObj = new Button(context);
		btnObj.setTag(tag);
		btnObj.setText(text);
		btnObj.setFocusable(true);
		btnObj.setGravity(Gravity.CENTER);
		btnObj.setHeight(65);
		btnObj.setWidth(width);
		btnObj.setTextSize(36);
		btnObj.setTextColor(textColor);
		btnObj.setBackgroundResource(btnBackground);
		btnObj.setOnClickListener(onClick);
		return btnObj;
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			switch (tag) {
			case Tag.KEY_UPPERCASE:
				createTableLayout(Tag.KEY_UPPERCASE);
				break;
			case Tag.KEY_LOWERCASE:
				createTableLayout(Tag.KEY_LOWERCASE);
				break;
			case Tag.KEY_SPECILKEY:
				createTableLayout(Tag.KEY_SPECILKEY);
				break;
			case Tag.KEY_BACKSPACE:
				String s = getText();
				if (s.length() > 1) {
					setText(s.substring(0, s.length() - 1));
				} else {
					setText("");
				}
				break;
			case Tag.KEY_CLEARUP:
				setText("");
				break;
			default:
				setText(getText() + ((Button) v).getText().toString());
				break;
			}
		}
	};

	private void setText(String text) {
		getTarget().setText(text);
	}

	private String getText() {
		return getTarget().getText().toString();
	}

	private EditText getTarget() {
		if (target == null) {
			target = (EditText) ((Activity) context).findViewById(targetID);
		}
		return target;
	}

	public void setTarget(int targetid) {
		targetID = targetid;
	}

	public void setEditTextView(EditText view) {
		target = view;
	}

	private class Tag {
		public final static int KEY_UPPERCASE = 0;
		public final static int KEY_LOWERCASE = 1;
		public final static int KEY_SPECILKEY = 2;
		public final static int KEY_NORMALKEY = 3;
		public final static int KEY_BACKSPACE = 4;
		public final static int KEY_CLEARUP = 5;
	}
}
