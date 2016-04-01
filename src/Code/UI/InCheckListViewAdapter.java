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

public class InCheckListViewAdapter extends BaseAdapter{

	LayoutInflater inflater;

	/**
	 * 要绑定的文章数据
	 */
	List<Article> articles;

	Context context;

	public InCheckListViewAdapter(List<Article> articles, Context context) {
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
			view = inflater.inflate(R.layout.check_item, parent, false);
		} else {
			view = convertView;
		}
		Article item = null;
		item = articles.get(position);

		TextView title_article = (TextView) view
				.findViewById(R.id.check_item_num);
		TextView content_article = (TextView) view
				.findViewById(R.id.check_item_content);
		TextView date_article = (TextView) view.findViewById(R.id.check_item_date);


		title_article.setText((position+1)+"");
		content_article.setText(item.getName());
		date_article.setText(item.getDate().toLocaleString());

		return view;
	}

	

	static String cutString(String source, int length, String addString) {

		if (source.length() <= length) {
			return source;
		}

		return source.substring(0, length) + addString;
	}
}






