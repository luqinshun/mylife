package com.ep.activiti.handlerUtil;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class TimestampToFormatStringHandler implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {
		
		Timestamp date = rs.getTimestamp(columnName);
		
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		Date date = rs.getDate(columnIndex);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	@Override
	public String getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, String arg2,
			JdbcType arg3) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	

}
