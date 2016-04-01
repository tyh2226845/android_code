package Code.UI;

import java.util.List;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddListViewAdapter extends BaseAdapter{

	LayoutInflater inflater;

	/**
	 * 要绑定的文章数据
	 */
	List<Article> articles;

	Context context;

	public AddListViewAdapter(List<Article> articles, Context context) {
		this.articles = articles;
		this.context = context;

		// 初始化一个填充对象
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 总数据
	 */
//	适配器更新
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
			view = inflater.inflate(R.layout.add_item, parent, false);
		} else {
			view = convertView;
		}
		Article item = null;
		item = articles.get(position);
		final int  item_id = item.getId();

		TextView title_article = (TextView) view
				.findViewById(R.id.add_item_num);
		TextView content_article = (TextView) view
				.findViewById(R.id.add_item_content);
		TextView date_article = (TextView) view.findViewById(R.id.add_item_date);
		Button button = (Button) view.findViewById(R.id.add_item_delete);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
				SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.DeleteArticleByKey(db, item_id);
				AddListViewAdapter.this.onDataChange(articleDao.GetAllPerpareArticles(db));
				db.close();
			}
		});

		title_article.setText((position+1)+"");
		content_article.setText(item.getName());
		date_article.setText(item.getDate().toLocaleString());

		return view;
	}

}






