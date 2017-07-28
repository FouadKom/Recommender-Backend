/*******************************************************************************
 * Copyright (C) 2017 TU Graz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TU Graz - initial API and implementation
 ******************************************************************************/
package at.tugraz.ist.agile.recommender.models;

import java.util.ArrayList;
import java.util.List;

public class ListOfClouds {
	
	private List<Cloud> cloudList = new ArrayList<Cloud>();

	public List<Cloud> getCloudList() {
		return cloudList;
	}

	public void setCloudList(List<Cloud> cloudList) {
		this.cloudList = cloudList;
	}

}
