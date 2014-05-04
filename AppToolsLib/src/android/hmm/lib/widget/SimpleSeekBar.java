package android.hmm.lib.widget;

/**
 * @author:heming
 * @since :JDK 17  
 * @version：1.0
 * Create at:2013-7-28
 * Description:  
 *
 */
public class SimpleSeekBar {

//	1.在res/drawable目录下新增一个xml风格文件，seekbar_define_style.xml
//	<?xml version="1.0" encoding="utf-8"?>
//	<layer-list
//		xmlns:android="http://schemas.android.com/apk/res/android">
//		<!-- 未选中 -->
//		<item
//			android:id="@android:id/background"
//			android:drawable="@drawable/hou"/>
//		<!-- 中 -->
//		<item
//			android:id="@android:id/progress"
//			android:drawable="@drawable/qian"/>
//		<item
//			android:id="@android:id/secondaryProgress"
//			android:drawable="@drawable/qian"/>
//	</layer-list> 
//	2.在res/drawable下定义个seekbar_thumb.xml文件 
//	<?xml version="1.0" encoding="utf-8"?>
//	<selector xmlns:android="http://schemas.android.com/apk/res/android">        
//	       
//	    <!-- 按下状态-->  
//	    <item    
//	        android:state_focused="true"    
//	        android:state_pressed="true"    
//	        android:drawable="@drawable/ic_launcher" />        
//	    <!-- 普通无焦点状态 -拖动按钮-->  
//	    <item    
//	        android:state_focused="false"    
//	        android:state_pressed="false"  
//	        android:drawable="@drawable/orbino_icon_pack_006" />              
//	    <!-- 有焦点状态-->  
//	    <item    
//	        android:state_focused="true"    
//	        android:state_pressed="false"              
//	        android:drawable="@drawable/ios" />         
//	    <!-- 有焦点 -->  
//	    <item    
//	        android:state_focused="true"              
//	        android:drawable="@drawable/ios"/>
//	</selector>  
//	3.在res/layut下定义布局资源文件seekbar_define.xml
//	<SeekBar
//	    android:layout_width="321px"
//	    android:layout_height="wrap_content"
//	    android:layout_centerInParent="true"
//	    android:maxHeight="20px"
//	    android:minHeight="20px"
//	    android:paddingLeft="18px"
//	    android:paddingRight="18px"
//	    android:max="100"
//	    android:progressDrawable="@drawable/seekbar_define_style"
//	    android:thumb="@drawable/seekbar_thumb"
//	    android:id="@+id/seekBar"/>
//	 </LinearLayout>

	
//	二：使用颜色显示，和尚面是一样的，只有我们定义颜色资源来替代图片资源文件seekbar_define_color_style.xml：如下：
//	seekbar_define_color_style.xml：如下：
//	<?xml version="1.0" encoding="UTF-8"?>     
//	<layer-list xmlns:android="http://schemas.android.com/apk/res/android">     
//	  
//	   <item android:id="@android:id/background"
//	            android:paddingTop="3px" 
//	         android:paddingBottom="3px">     
//	      <shape>     
//	         <corners android:radius="10dip" />     
//	         <gradient    
//	             android:startColor="#ffffffff"  
//	             android:centerColor="#ff000000"     
//	             android:endColor="#ff808A87"    
//	             android:centerY="0.45"     
//	             android:angle="270"/>     
//	      </shape>     
//	   </item>     
//	      
//	   <item android:id="@android:id/progress"
//	            android:paddingTop="3px" 
//	         android:paddingBottom="3px" >     
//	       <clip>     
//	          <shape>     
//	              <corners android:radius="10dip" />     
//	              <gradient    
//	                  android:startColor="#ffffffff"  
//	                  android:centerColor="#ffFFFF00"     
//	                  android:endColor="#ffAABD00"    
//	                  android:centerY="0.45"     
//	                  android:angle="270"/>     
//	          </shape>     
//	       </clip>     
//	   </item>     
//	 </layer-list>  
//	 之后再SeekBar标签使用如下属性进行引入：其他保持不变 
//	 android:progressDrawable="@drawable/seekbar_define_color_style"
//	 由于SeekBar的属性thumb引入了自定义的seekbar_thumb.xml文件，拖动图标是我们自定义的图片：除去这个属性
//	 android:thumb="@drawable/seekbar_thumb"
//	 就回复系统默认状态效果最后效果如下：
//	我们可以通过颜色值再次休息seekbar_thumb.xml文件，使拖动按钮设置成自定义颜色：
	
	
}
