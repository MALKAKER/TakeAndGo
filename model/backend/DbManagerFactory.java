package com.javaproject.malki.takeandgo.model.backend;

import com.javaproject.malki.takeandgo.model.DataSource.ListDbManager;
import com.javaproject.malki.takeandgo.model.DataSource.List_DB;
import com.javaproject.malki.takeandgo.model.DataSource.MySQL_DBManager;


/**
 * factory for the DB
 * Created by malki on 20-Nov-17.
 */

public class DbManagerFactory {


        //here the type of the DB , if the type is changed,
        //the developer needs only to change that line
        static DB_Manager manager = null;

        public static DB_Manager getManager() {
        if (manager == null)
            manager = new MySQL_DBManager();
        return manager;

    }
}
