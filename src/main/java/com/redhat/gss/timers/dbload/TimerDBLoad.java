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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

/**
 * Sample using the EJB's @Schedule and Native Management API for Transaction
 * removal.
 * 
 * Note that arbitrarily removing entries in the local transaction store can
 * result in recoverable transactions being lost
 * 
 * @author <a href="mailto:jolee@redhat.com">Johnathon Lee</a>
 */
@Singleton
public class TimerDBLoad {

	ModelControllerClient client = null;

	// For time-saving purposes of the sample this is set to run every 5 seconds
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void executeSQL() {

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
	        result = stmt.executeQuery("select * from ALL_TAB_COLUMNS");
	       }
	     }
	     catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error occurred " + e);
	      } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      int columns=0;
	      try {
	        rsmd = result.getMetaData();
	        columns = rsmd.getColumnCount();
	      }
	      catch (SQLException e) {
	         System.out.println("Error occurred " + e);
	      }

	      // write out the header cells containing the column labels
	      try {
	         for (int i=1; i<=columns; i++) {
	              
	         }
	         // now write out one row for each entry in the database table
	         while (result.next()) {
	            for (int i=1; i<=columns; i++) {
	            	System.out.println(rsmd.getColumnLabel(i) + ":" + result.getString(i));
	            }
	         }
	 
	         // close the connection, resultset, and the statement
	         result.close();
	         stmt.close();
	         conn.close();
	      } // end of the try block
	      catch (SQLException e) {
	         System.out.println("Error " + e);
	      }
	      // ensure everything is closed
	    finally {
	     try {
	       if (stmt != null)
	        stmt.close();
	       }  catch (SQLException e) {}
	       try {
	        if (conn != null)
	         conn.close();
	        } catch (SQLException e) {}
	    }
	}
}