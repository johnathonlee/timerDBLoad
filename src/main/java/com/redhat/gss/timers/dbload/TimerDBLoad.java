/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.gss.timers.dbload;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Sample using the EJB's @Schedule to run a simple Query
 * 
 * @author <a href="mailto:jolee@redhat.com">Johnathon Lee</a>
 */
@Singleton
public class TimerDBLoad {

	// For time-saving purposes of the sample this is set to run every 35 seconds
	@Schedule(second = "*/35", minute = "*", hour = "*", persistent = false)
	public void executeSQL() throws SQLException {

	    DataSource ds = null;
	    Connection conn = null;
	    ResultSet result = null;
	    Statement stmt = null;
	    ResultSetMetaData rsmd = null;
	    try{
	      Context context = new InitialContext();
	      Context envCtx = (Context) context.lookup("java:");
	      ds =  (DataSource)envCtx.lookup("/jdbc/jrl-ds");
	      if (ds != null) {
	        conn = ds.getConnection();
	        stmt = conn.createStatement();
	        result = stmt.executeQuery("select * from BEANONE");
	       }
	     } catch (SQLException e) {
	        System.out.println("----------------------------->Error occurred " + e.getErrorCode());
	    } catch (NamingException e) {
			e.printStackTrace();
		 } finally {
		     try {
			       if (stmt != null){
			        stmt.close();
			       }
			       } catch (SQLException e) {}
			       try {
			        if (conn != null){
			         conn.close();
			       }
			       } catch (SQLException e) {}
		}
	}
}