package com.wm.lock.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangmin on 16/6/27.
 */
class Sql {

    static final int VERSION_NEW = 3;
    private static Map<Integer, List<String>> map;

    static Map<Integer, List<String>> getMap() {
        if (map == null) {
            map = new ConcurrentHashMap<>();

            List<String> list1 = new ArrayList<>();
            list1.add("create table tb_push_message(id varchar(1000) primary key)");
            map.put(1, list1);

            List<String> list2 = new ArrayList<>();
            list2.add("drop table tb_push_message");
            list2.add("create table tb_push_message(\n" +
                    "id_ varchar(1000) primary key,\n" +
                    "id varchar(1000),\n" +
                    "type varchar(50),\n" +
                    "message varchar(500),\n" +
                    "extras text,\n" +
                    "createTime integer,\n" +
                    "lastModifyTime integer,\n" +
                    "state varchar(50),\n" +
                    "completeCode integer,\n" +
                    "completeDescription varchar(1000),\n" +
                    "userId varchar(200)" +
                    ")");
            map.put(2, list2);

            List<String> list3 = new ArrayList<>();
            list3.add("alter table tb_push_message add alert text");
            list3.add("alter table tb_push_message add senderId varchar(200)");
            list3.add("alter table tb_push_message add senderName varchar(50)");
            list3.add("alter table tb_push_message add createdDate integer");
            list3.add("alter table tb_push_message add lastModifiedDate integer");
            map.put(3, list3);
        }
        return map;
    }

    static void destroy() {
        map = null;
    }

}
