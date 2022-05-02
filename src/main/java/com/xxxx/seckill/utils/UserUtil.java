package com.xxxx.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.vo.RespBean;

import javax.naming.PartialResultException;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生产用户工具类
 * @author yangWu
 * @date 2022/4/22 10:25 PM
 * @version 1.0
 */
public class UserUtil {
    private static <ChunkedInputStream> void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(14000000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setLoginCount(0);
            user.setRegisterDate(new Date());
            users.add(user);
        }
        System.out.println("create user");

        // 插入数据库
        Connection conn = getConn();
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id) " +
            "values(?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        for (int i = 0; i < count; i++) {
            User user = users.get(i);
            preparedStatement.setInt(1, user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        conn.close();
        System.out.println("insert to db");

        // 登录，获取 ticket
        // 注意接口是 doLogin 不是 toLogin，调试了 2 小时 (获取 B 站弹幕)
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("/Users/yangwu/codebase/java-projects/spring/seckill-demo/src/main/resources/jmeter/config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < count; i++) {
            Long userId = users.get(i).getId();
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) (url.openConnection());
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            String params = "mobile=" + userId + "&password=" + MD5Util.inputPassToFormPass("123456");
            outputStream.write(params.getBytes());
            outputStream.flush();
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();

            // 获取 ticket (cookie)
            // String ticket = httpURLConnection.getHeaderField("Set-Cookie");
            // ticket = ticket.substring(ticket.indexOf("=") + 1, ticket.indexOf(";"));
            // System.out.println("create ticket :" + userId);
            String response = bout.toString();
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String ticket = (String) respBean.getObj();

            // 写入文件
            String row = userId + "," + ticket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r".getBytes());
            System.out.println("write to file :" + userId);
        }

        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "185530";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
