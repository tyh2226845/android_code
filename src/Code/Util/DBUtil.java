package Code.Util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBUtil {
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();

	public static Connection getConnection() {
		Connection con = null;
		try {
			// Class.forName("org.gjt.mm.mysql.Driver");
			// con=DriverManager.getConnection("jdbc:mysql://192.168.0.106:3306/test?useUnicode=true&characterEncoding=UTF-8","root","initial");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return con;
	}

	/**
	 * 获取所有货物的信息
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectET_InWork() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		arrayList.clear();
		brrayList.clear();
		crrayList.clear();

		crrayList = Soap.GetWebServre("selectET_InWork", arrayList, brrayList);

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("uInwId", "入库");
		tempHash.put("cworkformid", "作业单号");
		tempHash.put("compactid", "合同编号");
		tempHash.put("client", "客户名称");
		list.add(tempHash);

		for (int j = 0; j < crrayList.size(); j += 4) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("uInwId", crrayList.get(j));
			hashMap.put("cworkformid", crrayList.get(j + 1));
			hashMap.put("compactid", crrayList.get(j + 2));
			hashMap.put("client", crrayList.get(j + 3));
			list.add(hashMap);
		}

		return list;
	}

	public List<HashMap<String, String>> selectET_OutWork() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		arrayList.clear();
		brrayList.clear();
		crrayList.clear();

		crrayList = Soap.GetWebServre("selectET_OutWork", arrayList, brrayList);

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("uOutwId", "出库");
		tempHash.put("cWorkFormId", "作业单号");
		tempHash.put("compactid", "合同编号");
		tempHash.put("Client", "客户名称");
		list.add(tempHash);

		for (int j = 0; j < crrayList.size(); j += 4) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("uOutwId", crrayList.get(j));
			hashMap.put("cWorkFormId", crrayList.get(j + 1));
			hashMap.put("compactid", crrayList.get(j + 2));
			hashMap.put("Client", crrayList.get(j + 3));
			list.add(hashMap);
		}

		return list;
	}


	/**
	 * 增加一条货物信息
	 * 
	 * @return
	 */
	public void insertCargoInfo(String Cname, String Cnum) {

		arrayList.clear();
		brrayList.clear();

		arrayList.add("Cname");
		arrayList.add("Cnum");
		brrayList.add(Cname);
		brrayList.add(Cnum);

		Soap.GetWebServre("insertCargoInfo", arrayList, brrayList);
	}

	/**
	 * 删除一条货物信息
	 * 
	 * @return
	 */
	public void deleteCargoInfo(String Cno) {

		arrayList.clear();
		brrayList.clear();

		arrayList.add("Cno");
		brrayList.add(Cno);

		Soap.GetWebServre("deleteCargoInfo", arrayList, brrayList);
	}
}
