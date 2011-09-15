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
package it.eng.spagobi.commons.serializer;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.analiticalmodel.document.bo.ObjNote;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.analiticalmodel.execution.service.GetParametersForExecutionAction;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.commons.bo.Config;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarm;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarmContact;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.Periodicity;
import it.eng.spagobi.kpi.goal.metadata.bo.Goal;
import it.eng.spagobi.kpi.goal.metadata.bo.GoalNode;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelExtended;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.bo.ModelResourcesExtended;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnit;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnitGrant;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnitHierarchy;
import it.eng.spagobi.kpi.ou.bo.OrganizationalUnitNodeWithGrant;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bo.UserBO;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.bo.JavaClassDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WebServiceDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.metadata.SbiCustomDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiJClassDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiQueryDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiScriptDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiWSDataSet;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;
import it.eng.spagobi.tools.udp.metadata.SbiUdp;
import it.eng.spagobi.tools.udp.metadata.SbiUdpValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JSONSerializer implements Serializer {
	
	Map<Class, Serializer> mappings;
	
	public JSONSerializer() {
		mappings = new HashMap();
		mappings.put( Domain.class, new DomainJSONSerializer() );
		mappings.put( Config.class, new ConfigJSONSerializer() );
		mappings.put( Engine.class, new EngineJSONSerializer() );
		mappings.put( Role.class, new RoleJSONSerializer() );
		mappings.put( BIObject.class, new DocumentsJSONSerializer() );
		mappings.put( LowFunctionality.class, new FoldersJSONSerializer() );
		mappings.put( SubObject.class, new SubObjectsJSONSerializer() );
		mappings.put( Viewpoint.class, new ViewpointJSONSerializer() );
		mappings.put( Snapshot.class, new SnapshotJSONSerializer() );
		mappings.put( DataStore.class, new DataStoreJSONSerializer() );
		mappings.put( ObjNote.class, new ObjectNotesJSONSerializer() );
		mappings.put( ObjMetaDataAndContent.class, new MetadataJSONSerializer());	
		mappings.put( ObjMetadata.class, new ShortMetadataJSONSerializer());	
		mappings.put( SbiUser.class, new SbiUserJSONSerializer());
		mappings.put( UserBO.class, new UserJSONSerializer());
		mappings.put( SbiAttribute.class, new AttributeJSONSerializer());
		mappings.put( SbiAlarm.class, new AlarmJSONSerializer());
		mappings.put( SbiAlarmContact.class, new AlarmContactJSONSerializer());
		mappings.put( ThresholdValue.class, new ThresholdValueJSONSerializer());
		mappings.put( SbiDataSetConfig.class, new SbiDataSetConfigJSONSerializer());
		mappings.put( SbiQueryDataSet.class, new SbiDataSetConfigJSONSerializer());
		mappings.put( SbiJClassDataSet.class, new SbiDataSetConfigJSONSerializer());
		mappings.put( SbiScriptDataSet.class, new SbiDataSetConfigJSONSerializer());
		mappings.put( SbiWSDataSet.class, new SbiDataSetConfigJSONSerializer());
		mappings.put( SbiCustomDataSet.class, new SbiDataSetConfigJSONSerializer());
		
		mappings.put( GuiGenericDataSet.class, new DataSetJSONSerializer());
		mappings.put( Resource.class, new ResourceJSONSerializer());
		mappings.put( Threshold.class, new ThresholdJSONSerializer());
		mappings.put( Kpi.class, new KpiJSONSerializer());
		mappings.put( Periodicity.class, new PeriodicityJSONSerializer());
		
		mappings.put( Model.class, new ModelNodeJSONSerializer());	
		mappings.put( ModelInstance.class, new ModelInstanceNodeJSONSerializer());
		mappings.put( ModelResourcesExtended.class, new ModelResourcesExtendedJSONSerializer());
		mappings.put( ModelExtended.class, new ModelExtendedJSONSerializer());
		
		mappings.put( GetParametersForExecutionAction.ParameterForExecution.class, new ParameterForExecutionJSONSerializer() );
		mappings.put( SbiUdp.class, new UdpJSONSerializer());
		mappings.put( SbiUdpValue.class, new UdpValueJSONSerializer());
		
		mappings.put( OrganizationalUnitGrant.class, new OrganizationalUnitGrantJSONSerializer());
		mappings.put( OrganizationalUnit.class, new OrganizationalUnitJSONSerializer());
		mappings.put( OrganizationalUnitHierarchy.class, new OrganizationalUnitHierarchyJSONSerializer());
		mappings.put( OrganizationalUnitNodeWithGrant.class, new OrganizationalUnitNodeWithGrantJSONSerializer());
		
		mappings.put( GoalNode.class, new GoalNodeJSONSerializer());
		mappings.put( Goal.class, new GoalJSONSerializer());
	}

	public Object serialize(Object o, Locale locale) throws SerializationException {
		Object result = null;	
		
		try {
			if(o instanceof Collection) {
				JSONArray r = new JSONArray();
				Collection c = (Collection)o;
				Iterator it = c.iterator();
				while(it.hasNext()) {
					r.put( serialize( it.next() ,locale) );
				}
				result = r;
			} else {
				if( !mappings.containsKey(o.getClass())) {
					throw new SerializationException("JSONSerializer is unable to serialize object of type: " + o.getClass().getName());
				}
				
				Serializer serializer = mappings.get(o.getClass());
				result = serializer.serialize(o,locale);
			}			
			
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			
		}
		
		return result;	
	}


}
