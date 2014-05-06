/**
 * Copyright 1996-2014 FoxBPM Co.,Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author kenshin
 * @author ych
 */
package org.foxbpm.engine.impl.bpmn.parser.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.util.EList;
import org.foxbpm.engine.impl.bpmn.behavior.BaseElementBehavior;
import org.foxbpm.engine.impl.bpmn.parser.BpmnModelUtil;
import org.foxbpm.engine.impl.connector.Connector;
import org.foxbpm.engine.impl.connector.ConnectorInputParam;
import org.foxbpm.engine.impl.connector.ConnectorOutputParam;
import org.foxbpm.engine.impl.expression.ExpressionImpl;
import org.foxbpm.engine.impl.util.StringUtil;
import org.foxbpm.model.bpmn.foxbpm.ConnectorInstance;
import org.foxbpm.model.bpmn.foxbpm.ConnectorParameterInput;
import org.foxbpm.model.bpmn.foxbpm.ConnectorParameterOutput;
import org.foxbpm.model.bpmn.foxbpm.FoxBPMPackage;

public class BaseElementParser {
	
	protected BaseElementBehavior baseElementBehavior;
	
	protected List<Connector> connectors=new ArrayList<Connector>();
	
	
	
	public List<Connector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<Connector> connectors) {
		this.connectors = connectors;
	}

	/**
	 * @param baseElement
	 * @return
	 */
	public BaseElementBehavior parser(BaseElement baseElement){
		 
		List<ConnectorInstance> connectorInstances=  BpmnModelUtil.getExtensionElementList(ConnectorInstance.class,baseElement,FoxBPMPackage.Literals.DOCUMENT_ROOT__CONNECTOR_INSTANCE);
		
		
		for (ConnectorInstance connectorInstance : connectorInstances) {
			String packageNamesString = connectorInstance.getPackageName();
			String classNameString = connectorInstance.getClassName();
			String eventTypeString = connectorInstance.getEventType();
			String connectorIdString = connectorInstance.getConnectorId();
			String connectorInstanceIdString = connectorInstance.getConnectorInstanceId();
			String connectorInstanceNameString = connectorInstance.getConnectorInstanceName();
			String errorHandlingString = connectorInstance.getErrorHandling();
			String errorCodeString = connectorInstance.getErrorCode();
			boolean isTimeExecute = connectorInstance.isIsTimeExecute();
			String documentationString = connectorInstance.getDocumentation().getValue();
			String skipExpression = null;
			if (connectorInstance.getSkipComment() != null) {
				skipExpression = connectorInstance.getSkipComment().getExpression().getValue();
			}
			String timeExpression = null;
			if (connectorInstance.getTimeExpression() != null) {
				timeExpression = connectorInstance.getTimeExpression().getExpression().getValue();
			}
			Connector connectorInstanceBehavior = new Connector();
			connectorInstanceBehavior.setConnectorId(connectorIdString);
			connectorInstanceBehavior.setConnectorInstanceId(connectorInstanceIdString);
			connectorInstanceBehavior.setClassName(classNameString);
			connectorInstanceBehavior.setConnectorInstanceName(connectorInstanceNameString);
			connectorInstanceBehavior.setDocumentation(documentationString);
			connectorInstanceBehavior.setErrorCode(errorCodeString);
			connectorInstanceBehavior.setErrorHandling(errorHandlingString);
			connectorInstanceBehavior.setEventType(eventTypeString);
			connectorInstanceBehavior.setPackageName(packageNamesString);
			connectorInstanceBehavior.setSkipExpression(new ExpressionImpl(skipExpression));
			if (isTimeExecute) {
				connectorInstanceBehavior.setTimeExecute(true);
				connectorInstanceBehavior.setTimeExpression(new ExpressionImpl(timeExpression));
			} else {
				connectorInstanceBehavior.setTimeExecute(false);
			}
			
			
			List<ConnectorInputParam> connectorInputParameters=new ArrayList<ConnectorInputParam>();
			
			List<ConnectorOutputParam> connectorOutputParameters=new ArrayList<ConnectorOutputParam>();
					
					
			EList<ConnectorParameterInput> connectorParameterInputs = connectorInstance.getConnectorParameterInputs();
			EList<ConnectorParameterOutput> connectorParameterOutputs = connectorInstance.getConnectorParameterOutputs();
			
			
			for (ConnectorParameterInput connectorInputParamEmf : connectorParameterInputs) {
				
				ConnectorInputParam connectorInputParam=new ConnectorInputParam();
				connectorInputParam.setId(connectorInputParamEmf.getId());
				connectorInputParam.setExecute(StringUtil.getBoolean(connectorInputParamEmf.getIsExecute()));
				connectorInputParam.setDataType(connectorInputParamEmf.getDataType());
				connectorInputParam.setName(connectorInputParamEmf.getName());
				if(connectorInputParamEmf.getExpression()!=null){
					connectorInputParam.setExpression(new ExpressionImpl(connectorInputParamEmf.getExpression().getValue()));
				}
				connectorInputParameters.add(connectorInputParam);
			}
			
			
			for (ConnectorParameterOutput connectorOutputParamEmf : connectorParameterOutputs) {
				ConnectorOutputParam connectorOutputParam=new ConnectorOutputParam();
				connectorOutputParam.setOutputId(connectorOutputParamEmf.getOutputId());
				connectorOutputParam.setVariableTarget(connectorOutputParamEmf.getVariableTarget());
				connectorOutputParameters.add(connectorOutputParam);
			}

			
			connectors.add(connectorInstanceBehavior);
		}
		

		return baseElementBehavior;
	}
	
	public void init(){
		baseElementBehavior = new BaseElementBehavior();
	}

}