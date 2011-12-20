/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.scheduler.utils;

import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class BIObjectParametersIterator {

	/** A List of BIObjectParameter objects, each one containing a list of values. */
	private List cartproduct;

	/** A List of iterators storing the tuple-iterator's state. */
	private Map currstate = new HashMap();

	private List nextelt = new ArrayList();

	/** Constructor.
	 *
	 * @param   cartproduct  a List of BIObjectParameter objects from which the 
	 *                       tuples' components are to be draw.
	 */
	public BIObjectParametersIterator(List cartproduct) {
		this.cartproduct = cartproduct;
		for (int i = 0; i < cartproduct.size(); i++) {
			BIObjectParameter parameter = (BIObjectParameter) cartproduct.get(i);
			List parameterValues = parameter.getParameterValues();
			if (parameterValues == null) {
				parameterValues = new ArrayList();
			}
			Iterator curIter = parameterValues.iterator();
			currstate.put(parameter.getParameterUrlName(), curIter);
			if (curIter.hasNext()) {
				BIObjectParameter clone = parameter.clone();
				if (parameter.isIterative()) {
					List firstValue = new ArrayList();
					firstValue.add(curIter.next());
					clone.setParameterValues(firstValue);
				}
				nextelt.add(clone);
			}
		}
	}

	/** 
	 * Returns true if there are any more elements in the Cartesian product 
	 * to return.  
	 */
	public boolean hasNext() {
		return (nextelt != null);
	}

	/** Returns another tuple not returned previously.
	 *
	 * <p>The iterator stores its state in the private member 
	 * <code>currstate</code> -- an ArrayList of the iterators of the 
	 * individual Collections of any BIObjectParameter in the cartesian product. 
	 * In the start state, each iterator returns a single element. Afterwards, while iterator #1 
	 * has anything to return, we replace the first element of the previous 
	 * tuple to obtain a new tuple. Once iterator #1 runs out of elements we 
	 * replace it and advance iterator #2. We keep on advancing iterator #1 
	 * until it runs out of elements for the second time, reinitialize it 
	 * again, and advance iterator #2 once more. We repeat these operations 
	 * until iterator #2 runs out of elements and we start advancing 
	 * iterator #3, and so on, until all iterators run out of elements.
	 * 
	 */
	public Object next() {
		if (nextelt == null) {
			throw new NoSuchElementException();
		}

		// It's important that we return a new list, not the list that we'll 
		// be modifying to create the next tuple (namely nextelt).  The 
		// caller might be storing some of these tuples in a collection, 
		// which would yield unexpected results if we just gave them the 
		// same List object over and over.  
		List result = new ArrayList(nextelt);

		// compute the next element
		boolean gotNext = false;
		for (int i = 0; i < cartproduct.size(); i++) {
			BIObjectParameter parameter = (BIObjectParameter) cartproduct.get(i);
			if (!parameter.isIterative()) continue;
			Iterator curIter = (Iterator) currstate.get(parameter.getParameterUrlName());
			if (curIter.hasNext()) {
				// advance this iterator, we have next tuple
				for (int j = 0; j < nextelt.size(); j++) {
					BIObjectParameter aParameter = (BIObjectParameter) nextelt.get(j);
					if (aParameter.getParameterUrlName().equals(parameter.getParameterUrlName())) {
						BIObjectParameter clone = parameter.clone();
						List nextValue = new ArrayList();
						nextValue.add(curIter.next());
						clone.setParameterValues(nextValue);
						nextelt.set(j, clone);
					}
				}
				gotNext = true;
				break;
			} else {
				// reset this iterator to its beginning, continue loop
				curIter = parameter.getParameterValues().iterator();
				currstate.put(parameter.getParameterUrlName(), curIter);
				for (int j = 0; j < nextelt.size(); j++) {
					BIObjectParameter aParameter = (BIObjectParameter) nextelt.get(j);
					if (aParameter.getParameterUrlName().equals(parameter.getParameterUrlName())) {
						BIObjectParameter clone = parameter.clone();
						List nextValue = new ArrayList();
						nextValue.add(curIter.next());
						clone.setParameterValues(nextValue);
						nextelt.set(j, clone);
					}
				}
			}
		}
		if (!gotNext) {
			nextelt = null;
		}

		return result;
	}

}
