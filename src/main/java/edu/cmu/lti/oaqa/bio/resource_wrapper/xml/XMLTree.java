package edu.cmu.lti.oaqa.bio.resource_wrapper.xml;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 */
public class XMLTree {
	private XMLNode root;
	
	/**
	 */
	public XMLTree(Document doc) {
		try {
			// Get first ELEMENT_NODE (DOCUMENT_TYPE_NODE has no children)
			Node temp = doc.getFirstChild();
			while (temp.getNodeType() != Node.ELEMENT_NODE) {
				temp = temp.getNextSibling();
			}
			// Recursively build tree via constructors
			this.root = new XMLNode(null, temp);
		} catch (NullPointerException ne) {
			this.root = null;
			System.out.println("XMLTree: null input");
		}
	}
	
	/**
	 * Return the root of the tree (parent is null).
	 * @return	the XMLNode root of the tree
	 */
	public XMLNode getRoot() {
		return this.root;
	}
	
	/**
	 */
	public ArrayList<XMLNode> findNodes(String name) {
		return this.root.findAll(name, true);
	}
	
	/**
	 */
	public XMLNode findFirstNode(String name) {
		return this.root.findOne(name, true);
	}
}
