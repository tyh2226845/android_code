package Code.UI;

import java.util.List;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import Code.Entites.PlanarWorkOut;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AddOutWorkNumberAdapter extends BaseAdapter{

	LayoutInflater inflater;

	/**
	 * 要绑定的文章数据
	 */
	List<PlanarWorkOut> planarWorkOuts;
	Context context;

	public AddOutWorkNumberAdapter(List<PlanarWorkOut> planarWorkOuts, Context context) {
		this.planarWorkOuts = planarWorkOuts;
		this.context = context;

		// 初始化一个填充对象
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 总数据
	 */
//	适配器更新
//	public void onDataChange(List<PlanarWorkOut> planarWorkOuts) {
//		this.planarWorkOuts.clear();
//		this.planarWorkOuts.addAll(planarWorkOuts);
//		this.notifyDataSetChanged();
//	}
	
	@Override
	public int getCount() {
		return planarWorkOuts.size();
	}
	public String getWorkId(int position) {
		return planarWorkOuts.get(position).getWorkId();
		
	}

	@Override
	public PlanarWorkOut getItem(int position) {
		return planarWorkOuts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.outworknumber_item, parent, false);
		} else {
			view = convertView;
		}
		PlanarWorkOut pwk = planarWorkOuts.get(position);
		
		
		String workId = pwk.getWorkId();
		String client = pwk.getClient();
		String transPort = pwk.getTranSport();
		Log.i("workId", workId+"");
		Log.i("client", client+"");
		TextView workId_txt = (TextView) view
				.findViewById(R.id.outworknumber_item_workId);
		workId_txt.setText(workId);
		TextView client_txt = (TextView) view.findViewById(R.id.outworknumber_item_tv_client);
		client_txt.setText(client);
		TextView transPort_txt = (TextView) view.findViewById(R.id.outworknumber_item_transPort);
		transPort_txt.setText(transPort);
		return view;
	}
}
