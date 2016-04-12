package Code.DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Code.Entites.Article;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ArticleDAO {
//content:0-待入/出，1-已入/出，2-已校验，3-已上传
	/**
	 * 查询所有文章
	 * work_number
	 * @param db
	 * @return
	 */
	public String[] GetAllWork_Number(SQLiteDatabase db) {
//		判断数据库是否为空
		String[] work_numbers = null;
		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select name from work_number",
								null);
				int cursorCount = cursor.getCount();
				int i = 0;
				work_numbers = new String[cursorCount];
				while (cursor.moveToNext()) {
					work_numbers[i] = cursor.getString(cursor
							.getColumnIndex("name"));
				i++;
				}
			} finally {
//				db.close();
			}

		}
		return work_numbers;
	}
	public List<Article> GetAllPerpareArticles(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();
//		判断数据库是否为空
		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles where content ='0'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));
					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
	public List<Article> GetAllPerpareArticles_out(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();
//		判断数据库是否为空
		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles_out where content ='0'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));
					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
	public List<Article> GetAllPerpareArticles_out_n(SQLiteDatabase db,String w_number) {
		List<Article> aritlces = new ArrayList<Article>();
//		判断数据库是否为空
		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles_out where content ='0' and w_number ='"+w_number+"'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));
					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
//查询已入库
	public List<Article> GetAllArticles(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date,Arcode from articles where content ='1'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));
					entity.setArcode(cursor.getString(cursor.getColumnIndex("Arcode")));
					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
	public List<Article> GetAllArticles_out(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date,w_number from articles_out where content ='1'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					entity.setWID(cursor.getString(cursor
							.getColumnIndex("w_number")));
					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
				
					
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
//	查询已校验
	public List<Article> GetAllCheckArticles(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles where content ='2'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
	public List<Article> GetAllCheckArticles_out(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles_out where content ='2'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			return aritlces;
		}
		return null;
	}
//	查询已上传
	public List<Article> GetAllUploadArticles(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles where content ='3'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
		}
		return aritlces;
	}
	public List<Article> GetAllUploadArticles_out(SQLiteDatabase db) {
		List<Article> aritlces = new ArrayList<Article>();

		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles_out where content ='3'",
								null);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				while (cursor.moveToNext()) {
					Article entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
					aritlces.add(entity);
				}
			} finally {
//				db.close();
			}
			
		}
		return aritlces;
	}

	public Article GetMyWay(SQLiteDatabase db, String code) {
		Article entity = null;
		if (db.isOpen()) {
			try {
				Cursor cursor = db
						.rawQuery(
								"select id,name,content,date from articles where name = ?",
								new String[] { code });
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				if (cursor.moveToNext()) {
					entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
				}
			} finally {
//				db.close();
			}
			return entity;
		}
		return entity;
	}

	/**
	 * 查询文章通过编号
	 * 
	 * @param db
	 * @return
	 */
	public Article GetArticleById(SQLiteDatabase db, int articleId) {
		Article entity = null;
		if (db.isOpen()) {
			try {
				Cursor cursor = db.rawQuery(
						"select id,name,content,date from articles where id=?",
						new String[] { articleId + "" });
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				if (cursor.moveToNext()) {
					entity = new Article();
					entity.setId(cursor.getInt(0));
					entity.setName(cursor.getString(cursor
							.getColumnIndex("name")));
					entity.setContent(cursor.getString(cursor
							.getColumnIndex("content")));

					String strdate = cursor.getString(cursor
							.getColumnIndex("date"));
					try {
						entity.setDate(sdf.parse(strdate));
					} catch (ParseException e) {
						entity.setDate(new Date());
						e.printStackTrace();
					}
				}
			} finally {
//				db.close();
			}
			return entity;
		}
		return entity;
	}

	public void AddWork_Number(SQLiteDatabase db, String code) {
		if (db.isOpen()) {
			try {
				db.execSQL(
						"insert into work_number(name) values(?)",
						new Object[] { code });
			} finally {
//				db.close();
			}
		}
	}
	/**
	 * 添加待入的文章
	 * 
	 * @param entity
	 */
	public void AddPerpareArticle(SQLiteDatabase db, Article entity) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"insert into articles(name,content,date) values(?,?,?)",
						new Object[] { entity.getName(), entity.getContent(),
								sdf.format(entity.getDate()) });
			} finally {
//				db.close();
			}
		}
	}
	public void AddPerpareArticle_out(SQLiteDatabase db, Article entity) {
//		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"insert into articles_out(name,content,date) values(?,?,?)",
						new Object[] { entity.getName(), entity.getContent(),
								sdf.format(entity.getDate()) });
			} finally {
//				db.close();
			}
		}
	public void AddPerpareArticle_out_n(SQLiteDatabase db, Article entity,String w_number) {
//		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"insert into articles_out(name,content,date,w_number) values(?,?,?,?)",
						new Object[] { entity.getName(), entity.getContent(),
								sdf.format(entity.getDate()), w_number});
			} finally {
//				db.close();
			}
		}
//	}
//	}
	/**
	 * 更新至已入的文章
	 * 
	 * @param entity
	 */
	public void UpdateArticle(SQLiteDatabase db, Article entity) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles set content=?,date=? where name=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()), entity.getName() });
			} finally {
//				db.close();
			}
		}
	}
	public void UpdateArticle(SQLiteDatabase db, Article entity,String user) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles set content=?,date=?,user=?,Arcode=? where name=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()),user,entity.getArcode(),entity.getName() });
			} finally {
//				db.close();
			}
		}
	}
	public void UpdateArticle_out_n(SQLiteDatabase db, Article entity,String work_number) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles_out set content=?,date=? where name=? and w_number=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()), entity.getName(),  work_number});
			} finally {
			}
		}
	}
	public void UpdateArticle_out_n(SQLiteDatabase db, Article entity,String work_number,String user) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles_out set content=?,date=?,user=? where name=? and w_number=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()),user, entity.getName(),  work_number});
			} finally {
			}
		}
	}
	public void UpdateArticle_out(SQLiteDatabase db, Article entity) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles_out set content=?,date=? where name=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()), entity.getName()});
			} finally {
			}
		}
	}
	
	public boolean updateArticle_out_tran(SQLiteDatabase db,List<Article> list){
		boolean flag = false;
		if (db.isOpen()) {
		
			db.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			try {
				for (Article article : list) {
					ContentValues values = new ContentValues();
					values.put("content", "3");
					values.put("date", sdf.format(date));
					db.update("articles_out", values, "name='"+article.getName()+"'", null);
				}
				db.setTransactionSuccessful();
				flag = true;
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
			} finally {
				db.endTransaction();
			}		
		}
		return flag;
		
	}
//	更新至已校验文章
//	更新至已上传

	public void DeleteWork_NumberByKey(SQLiteDatabase db, String code) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from work_number where name=?",
						new Object[] { code });
				
			} finally {
//				db.close();
			}
		}
	}
	/**
	 * 删除文章通过编号
	 * 
	 * @param entity
	 */
	public void DeleteArticleByKey(SQLiteDatabase db, int id) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL("delete from articles where id=?",
						new Object[] { id });
				
			} finally {
//				db.close();
			}
		}
	}
	public void DeleteArticle_outByKey(SQLiteDatabase db, int id) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL("delete from articles_out where id=?",
						new Object[] { id });
				
			} finally {
//				db.close();
			}
		}
	}
	public void DeleteAllPerpare(SQLiteDatabase db) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from articles where content='0'");
			} finally {
//				db.close();
			}
		}
	}
	public void DeleteAllPerpare_out(SQLiteDatabase db) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from articles_out where content='0'");
			} finally {
//				db.close();
			}
		}
	}
	public void DeleteAllPerpare_out_n(SQLiteDatabase db,String w_number) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from articles_out where content='0' and w_number='"+w_number+"'");
			} finally {
//				db.close();
			}
		}
	}
	
	public void DeleteAllUpload(SQLiteDatabase db) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from articles where content='3'");
			} finally {
//				db.close();
			}
		}
	}
	public void DeleteAllUpload_out(SQLiteDatabase db) {
		if (db.isOpen()) {
			try {
				db.execSQL("delete from articles_out where content='3'");
			} finally {
//				db.close();
			}
		}
	}

	// public void updateMyWay(SQLiteDatabase db, String name) {
	// if (db.isOpen()) {
	// try {
	// db.execSQL(
	// "update articles set content=1,date=getDate() where name=?",
	// new Object[] { name });
	// } finally {
	// db.close();
	// }
	// }
	// }

	/**
	 * 更新文章
	 * 
	 * @param entity
	 */
	public void updateMyWay(SQLiteDatabase db, Article entity, String Code) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL(
						"update articles set content=?,date=? where name=?",
						new Object[] { entity.getContent(),
								sdf.format(entity.getDate()), Code });
			} finally {
//				db.close();
			}
		}
	}

//	public void UpdateArticle(SQLiteDatabase db, Article entity) {
//		if (db.isOpen()) {
//			try {
//				SimpleDateFormat sdf = new SimpleDateFormat(
//						"yyyy-MM-dd hh:mm:ss");
//				db.execSQL(
//						"update articles set name=?,content=?,date=? where id=?",
//						new Object[] { entity.getName(), entity.getContent(),
//								sdf.format(entity.getDate()), entity.getId() });
//			} finally {
////				db.close();
//			}
//		}
//	}

	public void UpdateALL(SQLiteDatabase db, Article entity) {
		if (db.isOpen()) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				db.execSQL("update articles set content=? where content = '1'",
						new Object[] { entity.getContent() });
			} finally {
//				db.close();
			}
		}
	}
	
	public boolean UpdateTran(SQLiteDatabase db,List<Article> list){
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		Date now = new Date();
		if (db.isOpen()) {
			db.beginTransaction();
			try {
				for (Article article : list) {		
					ContentValues values = new ContentValues();
					values.put("content", "3");
					values.put("date", sdf.format(now));	
					db.update("articles", values, "name='"+article.getName()+"'", null);
				}
				db.setTransactionSuccessful();
				flag = true;
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
			} finally{
				db.endTransaction();
			}
		}
		return flag;
	}

}
