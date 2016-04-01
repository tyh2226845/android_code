package Code.UI;

import java.util.List;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OutUploadListViewAdapter extends BaseAdapter {

	LayoutInflater inflater;

	public interface ChangeTextListener{
		void changeText();
	}
	private ChangeTextListener mListener;
	
	public void setOnChangeTextListener(ChangeTextListener listener) {
		mListener = listener;
	}
	/**
	 * 要绑定的文章数据
	 */
	List<Article> articles;

	Context context;

	public OutUploadListViewAdapter(List<Article> articles, Context context) {
		this.articles = articles;
		this.context = context;

		// 初始化一个填充对象
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 总数据
	 */
	public void onDataChange(List<Article> mArticles) {
		articles.clear();
		articles.addAll(mArticles);
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return articles.size();
	}

	@Override
	public Object getItem(int position) {
		return articles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return articles.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.upload_item, parent, false);
		} else {
			view = convertView;
		}
		Article item = null;
		item = articles.get(position);
		final int item_id = item.getId();
		TextView content_article = (TextView) view
				.findViewById(R.id.upload_content);
		TextView date_article = (TextView) view.findViewById(R.id.upload_date);
		ImageButton iButton_article = (ImageButton) view
				.findViewById(R.id.upload_ib);

		content_article.setText(item.getName());
		date_article.setText(item.getDate().toLocaleString());
		iButton_article.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
				SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.DeleteArticle_outByKey(db, item_id);
				OutUploadListViewAdapter.this.onDataChange(articleDao.GetAllArticles_out(db));
				db.close();
				mListener.changeText();
			}
		});

		return view;
	}

}




