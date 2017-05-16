package com.wjj.weiguan.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjj.weiguan.db.DBHelper;
import com.wjj.weiguan.db.DBInfo;
import com.wjj.weiguan.pojo.User;

public class UserDao {
	private DBHelper dbHelper=null;
	private SQLiteDatabase db=null;

	public UserDao(Context context){
		dbHelper=new DBHelper(context);
	}
	
	//插入用户
	public void insertUser(User user){
		db=dbHelper.getWritableDatabase();
		db.execSQL("insert into "+DBInfo.Table.USER_TABLE+" ("
				+DBInfo.Table.USER_ID+","
				+DBInfo.Table.USER_NAME+","
				+DBInfo.Table.USER_HEAD+","
				+DBInfo.Table.TOKEN+","
				+DBInfo.Table.DESCRIPTION+","
				+DBInfo.Table.STATUSES_COUNT+","
				+DBInfo.Table.FRIENDS_COUNT+","
				+DBInfo.Table.FOLLOWERS_COUNT
				+") values ('"
				+user.getUser_id()+"','"
				+user.getUser_name()+"','"
				+user.getUser_head()+"','"
				+user.getToken()+"','"
				+user.getDescription()+"','"
				+user.getStatuses_count()+"','"
				+user.getFriends_count()+"','"
				+user.getFollowers_count()+"')"
				);
		db.close();
	}
	
	//更新用户
	public void updateUser(User user){
		db=dbHelper.getWritableDatabase();
		db.execSQL("update "+DBInfo.Table.USER_TABLE+" set "
				+DBInfo.Table.USER_NAME+"='"+user.getUser_name()+"',"
				+DBInfo.Table.USER_HEAD+"='"+user.getUser_head()+"',"
				+DBInfo.Table.DESCRIPTION+"='"+user.getDescription()+"' "
				+"where "
				+DBInfo.Table.USER_ID+"='"+user.getUser_id()+"'"
				);
		db.close();
	}
	
	//根据用户ID删除用户
	public int deleteUser(String user_id){
		return 1;
	}
	
	//根据用户ID查询用户
	public User findUserByUserId(String user_id){
		return null;
	}
	
	//查询集合中所有的用户
	public List<User> findAllUsers(){
		db=dbHelper.getWritableDatabase();
		List<User> userList=null;
		User user=null;
		Cursor cursor = db.rawQuery("select * from "+DBInfo.Table.USER_TABLE, null);
		if (cursor!=null && cursor.getCount()>0) {
			userList=new ArrayList<User>();
			while(cursor.moveToNext()){
				user=new User();
				user.setId(cursor.getLong(cursor.getColumnIndex(DBInfo.Table._ID)));
				user.setUser_id(cursor.getString(cursor.getColumnIndex(DBInfo.Table.USER_ID)));
				user.setUser_name(cursor.getString(cursor.getColumnIndex(DBInfo.Table.USER_NAME)));
				user.setUser_head(cursor.getString(cursor.getColumnIndex(DBInfo.Table.USER_HEAD)));
				user.setToken(cursor.getString(cursor.getColumnIndex(DBInfo.Table.TOKEN)));
				user.setDescription(cursor.getString(cursor.getColumnIndex(DBInfo.Table.DESCRIPTION)));
				user.setStatuses_count(cursor.getString(cursor.getColumnIndex(DBInfo.Table.STATUSES_COUNT)));
				user.setFriends_count(cursor.getString(cursor.getColumnIndex(DBInfo.Table.FRIENDS_COUNT)));
				user.setFollowers_count(cursor.getString(cursor.getColumnIndex(DBInfo.Table.FOLLOWERS_COUNT)));
				userList.add(user);
			}
		}
		cursor.close();
		db.close();

		return userList;
	}
}
