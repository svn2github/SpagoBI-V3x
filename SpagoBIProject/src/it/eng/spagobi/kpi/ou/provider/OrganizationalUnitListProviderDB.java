/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.kpi.ou.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnit;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnitHierarchy;
import it.eng.spagobi.utilities.tree.Node;
import it.eng.spagobi.utilities.tree.Tree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class OrganizationalUnitListProviderDB extends
		OrganizationalUnitListProvider {

	String S = Tree.NODES_PATH_SEPARATOR;
	static private Logger logger = Logger
			.getLogger(OrganizationalUnitListProviderDB.class);
	
	private static String jndiDatasource;
	private static final String HIERARCHY = "hierarchy";
	private static final String OU = "ou";
	private static final String COMPANY = "company"; 
	private static final String CODE = "code";
	private static final String NAME = "name";
	
	private String getHierarchiesQuery = null;
	private String getOUsQuery = null;
	private String getRootByHierarchy = null;
	private String getChildrenByLevel = null;
	private String getRootLeaves = null;
	/*
	 * tabella dws_t_an_aggr_ce 
	 * ag_tab nome gerarchia 
	 * ag_cetabl1.....15 codici uo 
	 * ag_detabl1....15 sono i nomi delle UO 
	 * 15 livelli di profondit� 
	 * AG_DIM � il codice foglia 
	 * cd_az � il codice azienda
	 */	
	@Override
	public void initialize() {		
		SourceBean ouConfig = (SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.ORGANIZATIONAL-UNIT");

		jndiDatasource = (String) ouConfig.getAttribute("jndiDatasource");
		getHierarchiesQuery = (String) ouConfig.getAttribute("getHierarchiesQuery");
		getOUsQuery= (String) ouConfig.getAttribute("getOUsQuery");
		getRootByHierarchy = (String) ouConfig.getAttribute("getRootByHierarchy");
		getChildrenByLevel= (String) ouConfig.getAttribute("getChildrenByLevel");
		getRootLeaves= (String) ouConfig.getAttribute("getRootLeaves");
	}

	 static Connection getJNDIConnection() {

		Connection result = null;
		try {
			Context initialContext = new InitialContext();
			if (initialContext == null) {
				logger.error("JNDI problem. Cannot get InitialContext.");
			}
			DataSource datasource = (DataSource) initialContext
					.lookup(jndiDatasource);
			if (datasource != null) {
				result = datasource.getConnection();
			} else {
				logger.error("Failed to lookup datasource.");
			}
		} catch (NamingException ex) {
			logger.error("Cannot get connection: " + ex.getMessage());
		} catch (SQLException ex) {
			logger.error("Cannot get connection: " + ex.getMessage());
		}
		return result;
	}
	@Override
	public List<OrganizationalUnitHierarchy> getHierarchies() {
		List<OrganizationalUnitHierarchy> toReturn = new ArrayList<OrganizationalUnitHierarchy>();

		try {
			executeQuery(getHierarchiesQuery, HIERARCHY, toReturn);

		} catch (Exception e) {
			logger.error("Error getting hiererchies list");
		}
		return toReturn;
	}

	@Override
	public List<OrganizationalUnit> getOrganizationalUnits() {
		List<OrganizationalUnit> toReturn = new ArrayList<OrganizationalUnit>();
		for(int i= 1; i<= 15 ; i++){
			try {
				String replacedQuery = getOUsQuery.replaceAll("\\!", Integer.toString(i));
				boolean isToBreak = executeQuery(replacedQuery, OU, toReturn);
				if(isToBreak){
					continue;
					
				}
			} catch (Exception e) {
				logger.error("Error getting OU list");
			}
		}
		return toReturn;
	}
	
	@Override
	public Tree<OrganizationalUnit> getHierarchyStructure(
			OrganizationalUnitHierarchy hierarchy) {
		OrganizationalUnit root = new OrganizationalUnit();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(HIERARCHY, hierarchy.getName());
		params.put(COMPANY, hierarchy.getCompany());
		Tree<OrganizationalUnit> toReturn = null;
		try {
			Node<OrganizationalUnit> rootNode = getRootByQueryString(getRootByHierarchy, params, null);
			if(rootNode != null){
				toReturn = new Tree<OrganizationalUnit>(rootNode);
				//get root leaves
				List<Node<OrganizationalUnit>> children = getRootLeaves(hierarchy.getName(), hierarchy.getCompany(), rootNode);
				//get root children nodes
				getChildrenByLevel(hierarchy.getName(), hierarchy.getCompany(), rootNode);

			}
		} catch (Exception e) {
			logger.error("Unable to get root node for hiererchy "+hierarchy.getName());
		}finally{
			return toReturn;
		}

	}
	private List<Node<OrganizationalUnit>> getRootLeaves (String hierarchy, String company, Node<OrganizationalUnit> parent){
		boolean isToBreak = false;
		List<Node<OrganizationalUnit>> children = new ArrayList<Node<OrganizationalUnit>>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(HIERARCHY, hierarchy);
		params.put(COMPANY, company);
		try {
			children = getLeavesByQueryString(getRootLeaves, params, parent);
			if(children == null || children.isEmpty()){
				isToBreak = true;
			}	

		} catch (Exception e) {
			logger.error("Unable to get node for hiererchy "+hierarchy);
		}finally{
			return children;
		}
		
	}

	private boolean executeQuery(String sqlQuery, String type, List toReturn) throws Exception {
		Connection con = null;
		boolean isToBreak = false;
		try {

			con = getJNDIConnection();
			Statement stmt = con.createStatement();
			logger.debug(sqlQuery);
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {

				if(type.equals(HIERARCHY)){						
					String hierName =  rs.getString("HIERARCHY");
					String company =  rs.getString("COMPANY");
					if(hierName != null){					
						OrganizationalUnitHierarchy item = new OrganizationalUnitHierarchy(null, hierName, hierName, null, null, company);
						toReturn.add(item);
					}

				}else if(type.equals(OU)){						

					String ouName =  rs.getString("NAME");
					String ouCode =  rs.getString("CODE");
					if(ouCode != null){
						OrganizationalUnit item = new OrganizationalUnit(null, ouCode, ouName, null);
						toReturn.add(item);
					}else{
						isToBreak = true;
						break;
					}
				}

			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			logger.error("Unable to execute query :"+e.getMessage());
		}finally{
			if(con != null)
				con.close();
			return isToBreak;
		}
	}	
	private boolean getChildrenByLevel (String hierarchy, String company, Node<OrganizationalUnit> parent){
		boolean isToBreak = false;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put(HIERARCHY, hierarchy);
		params.put(COMPANY, company);
		try {
			List<Node<OrganizationalUnit>> children  = getNodeByQueryString(getChildrenByLevel, params, parent);
			if(children == null || children.isEmpty()){
				isToBreak = true;
			}	

		} catch (Exception e) {
			logger.error("Unable to get node for hiererchy "+hierarchy);
		}finally{
			return isToBreak;
		}
		
	}
	private Node<OrganizationalUnit> getRootByQueryString(String sqlQuery, HashMap<String, String> parameters, Node<OrganizationalUnit> ouParentNode) throws Exception {
		Node<OrganizationalUnit> root = null;
		Connection con = null;
		try {

			con = getJNDIConnection();
			PreparedStatement pstmt = con.prepareStatement(sqlQuery);
			pstmt.setString(1, (String)parameters.get(HIERARCHY));
			pstmt.setString(2, (String)parameters.get(COMPANY));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				String code =  rs.getString(CODE);
				String name =  rs.getString(NAME);
				
				OrganizationalUnit ou =new OrganizationalUnit();
				ou.setDescription("");
				ou.setLabel(code);
				ou.setName(name);
				String path = "";
				if(ouParentNode != null){
					path = ouParentNode.getPath();
				}
				if(code != null){
					root = new Node<OrganizationalUnit>(ou, path + S + ou.getLabel(), ouParentNode);				
				}

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			logger.error("Unable to get root by query :"+sqlQuery);
		}finally{
			if(con != null)
				con.close();
			return root;
		}
	}
	private List<Node<OrganizationalUnit>> getLeavesByQueryString(String query, HashMap<String, String> parameters, Node<OrganizationalUnit> parent) throws Exception {
		List<Node<OrganizationalUnit>> children = new ArrayList<Node<OrganizationalUnit>>();
		Connection con = null;

		try {

			con = getJNDIConnection();
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, (String)parameters.get(HIERARCHY));
			pstmt.setString(2, (String)parameters.get(COMPANY));


			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				String code =  rs.getString(CODE);
				String name =  rs.getString(NAME);
				
				OrganizationalUnit ou =new OrganizationalUnit();
				ou.setDescription("");
				ou.setLabel(code);
				ou.setName(name);
				String path =  parent.getPath();

				if(code != null){
					Node<OrganizationalUnit> node = new Node<OrganizationalUnit>(ou, path + S+ ou.getLabel(), parent);		
					children.add(node);
				}
			}
			rs.close();
			pstmt.close();
			parent.setChildren(children);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.error("Unable to get node by query :"+sqle.getMessage());

		}finally{
			if(con != null)
				con.close();
			return children;
		}
	}
	private List<Node<OrganizationalUnit>> getNodeByQueryString(String query, HashMap<String, String> parameters, Node<OrganizationalUnit> parent) throws Exception {
		List<Node<OrganizationalUnit>> children = parent.getChildren();
		if(children == null){
			children = new ArrayList<Node<OrganizationalUnit>>();
		}
		int level = 2;

		if(parent != null){
			StringTokenizer st = new StringTokenizer(parent.getPath(), S, false);
			level = st.countTokens()+1;
		}
		logger.debug("level "+level+" for parent "+parent.getNodeContent().getLabel()+" with path:"+parent.getPath());
		Connection con = null;

		String replacedQuery = query.replaceAll("\\!", Integer.toString(level));
		replacedQuery = replacedQuery.replaceAll("\\%", Integer.toString(level-1));

		try {

			con = getJNDIConnection();
			PreparedStatement pstmt = con.prepareStatement(replacedQuery);
			pstmt.setString(1, (String)parameters.get(HIERARCHY));
			pstmt.setString(2, (String)parameters.get(COMPANY));
			pstmt.setString(3, parent.getNodeContent().getLabel());
			pstmt.setString(4, parent.getNodeContent().getName());


			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				String code =  rs.getString(CODE);
				String name =  rs.getString(NAME);
				
				OrganizationalUnit ou =new OrganizationalUnit();
				ou.setDescription("");
				ou.setLabel(code);
				ou.setName(name);
				String path = "";
				if(level != 1){
					path = parent.getPath();
				}
				if(code != null){
					Node<OrganizationalUnit> node = new Node<OrganizationalUnit>(ou, path + S+ ou.getLabel(), parent);		
					children.add(node);
				}
			}
			rs.close();
			pstmt.close();
			
			///sets children to parent node
			parent.setChildren(children);

			for(int i=0 ; i<children.size(); i++){
				Node<OrganizationalUnit> node = (Node<OrganizationalUnit>)children.get(i);
				if(level == 15){
					break;
				}
				getNodeByQueryString(query, parameters, node);

			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.error("Unable to get node by query :"+sqle.getMessage());

		}finally{
			if(con != null)
				con.close();
			return children;
		}
	}


}
