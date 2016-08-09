package com.ep.activiti.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ep.activiti.entity.TaskListenerHisAct;

public class QueryUtil {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://123.56.165.120:3308/db_activiti?useUnicode=true&amp;characterEncoding=UTF-8";

	public List<TaskListenerHisAct> getListenerHisTask(String processInstanceId) {

		Connection conn = null;
		PreparedStatement stmt = null;

		List<TaskListenerHisAct> list = new ArrayList<TaskListenerHisAct>();

		try {
			Class.forName(JDBC_DRIVER);
			conn =  DriverManager.getConnection(DB_URL, "dbac1o", "DBac5899!");
			

			String sql = "select a.ACT_NAME_,a.ASSIGNEE_,p.START_USER_ID_ from act_hi_actinst a INNER JOIN act_hi_procinst p on a.PROC_INST_ID_ = p.ID_ where a.PROC_INST_ID_ = "
					+ processInstanceId + " ORDER BY a.START_TIME_ ASC";
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String taskName = rs.getString("ACT_NAME_");
				String assignee = rs.getString("ASSIGNEE_");
				String startUserId = rs.getString("START_USER_ID_");
				TaskListenerHisAct t = new TaskListenerHisAct();
				t.setTaskName(taskName);
				t.setAssignee(assignee);
				t.setStartUserId(startUserId);
				list.add(t);
			}
			rs.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
}
