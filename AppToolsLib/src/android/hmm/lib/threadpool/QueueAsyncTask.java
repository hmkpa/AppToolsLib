package android.hmm.lib.threadpool;

import android.os.AsyncTask;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月10日
 * Description:  
 */
public class QueueAsyncTask extends AsyncTask<Object , Object, Object> {

	public QueueAsyncTask() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		if(isCancelled()){
			return null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		if(isCancelled()){
			return ;
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		if(isCancelled()){
			return ;
		}
		super.onProgressUpdate(values);
	}
	
	public void destory() {
		if (getStatus() != AsyncTask.Status.FINISHED) {
			cancel(true);// 如果Task还在运行，则先取消它，然后执行关闭activity代码
		}
	}
	

}
