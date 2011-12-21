/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.commons.presentation.tags;

import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

public class CommonWizardLovTag extends TagSupport {

	protected IMessageBuilder msgBuilder = null;
	protected String _bundle = null;
	protected HttpServletRequest httpRequest = null;
	
	protected String generateProfAttrTitleSection(String urlImg) {
	
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		if (_bundle == null)
			_bundle = "messages";

		httpRequest = (HttpServletRequest) pageContext.getRequest();
		
		StringBuffer output = new StringBuffer();
		output.append("		<td class='titlebar_level_2_empty_section'>&nbsp;</td>\n");
		output.append("		<td class='titlebar_level_2_button_section'>\n");
		output.append("			<a style='text-decoration:none;' href='javascript:opencloseProfileAttributeWin()'> \n");
		output.append("				<img width='22px' height='22px'\n");
		output.append("				 	 src='" + urlImg +"'\n");
		output.append("					 name='info'\n");
		output.append("					 alt='"+msgBuilder.getMessage("SBIDev.lov.avaiableProfAttr", _bundle,httpRequest)+"'\n");
		output.append("					 title='"+msgBuilder.getMessage("SBIDev.lov.avaiableProfAttr", _bundle, httpRequest)+"'/>\n");		
		//output.append("					 alt='"+PortletUtilities.getMessage("SBIDev.lov.avaiableProfAttr", "messages")+"'\n");
		//output.append("					 title='"+PortletUtilities.getMessage("SBIDev.lov.avaiableProfAttr", "messages")+"'/>\n");
		output.append("			</a>\n");
		output.append("		</td>\n");
		String outputStr = output.toString();
		return outputStr;
	}
	
}
