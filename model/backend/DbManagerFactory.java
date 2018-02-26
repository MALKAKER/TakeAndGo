package com.javaproject.malki.takeandgo.model.backend;

import com.javaproject.malki.takeandgo.model.DataSource.MySQL_DBManager;


/**
 * factory for the DB
 * Created by malki on 20-Nov-17.
 */

public class DbManagerFactory {




        static DB_Manager manager = null;
        /*
        * here the type of the DB , if the type is changed
        * */
        public static DB_Manager getManager() {
        if (manager == null)
            //the developer needs only to change that line
            manager = new MySQL_DBManager();
        return manager;

    }
}
