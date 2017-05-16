package com.wjj.weiguan.db;

public class DBInfo {
	public static class DB {
        /**
         * 数据库
         */
        public static final String DB_NAME = "weiguan.db";

        /**
         * 版本号
         */
        public static final int VERSION = 1;
    }

    public static class Table {
        public static final String USER_TABLE = "userinfo";
        
        public static final String _ID = "_id";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String TOKEN = "token";
        public static final String TOKEN_SECRET = "token_secret";
        public static final String DESCRIPTION = "description";
        public static final String USER_HEAD = "user_head";
        
        public static final String STATUSES_COUNT = "statuses_count";
        public static final String FRIENDS_COUNT = "friends_count";
        public static final String FOLLOWERS_COUNT = "followers_count";
        
        /**
         * 创建表
         */
        public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS  "
                + USER_TABLE
                + " ( _id INTEGER PRIMARY KEY autoincrement,user_id TEXT," +
                " user_name TEXT, token TEXT,token_secret TEXT," +
                "description TEXT,user_head TEXT,statuses_count TEXT," +
                "friends_count TEXT,followers_count TEXT)";
        /**
         * 删除表
         */
        public static final String DROP_USER_TABLE = "DROP TABLE "+ USER_TABLE;
    }
}
