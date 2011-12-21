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
package it.eng.spagobi.utilities.tree;

import java.util.List;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class Node<T> {
	private T object;
	private List<Node<T>> children;
	private String path;
	private Node<T> parent;
	
	public Node(T object, String path, Node<T> parent) {
		this.object = object;
		this.path = path;
		this.parent = parent;
	}
	public Node(T object, String path, Node<T> parent, List<Node<T>> children) {
		this.object = object;
		this.path = path;
		this.parent = parent;
		this.children = children;
	}
	public T getNodeContent() {
		return this.object;
	}
	public void setNodeContent(T object) {
		this.object = object;
	}
	public List<Node<T>> getChildren() {
		return children;
	}
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return this.path;
	}
	public Node<T> getParent() {
		return parent;
	}
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return "Node [object=" + object + ", path=" + path + "]";
	}
	
}
