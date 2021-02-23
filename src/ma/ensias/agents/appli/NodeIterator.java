package ma.ensias.agents.appli;

import java.io.Serializable;

import java.util.Iterator;

public class NodeIterator implements Iterator<Node>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 734271837570404007L;

	enum ProcessStages {
		ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	}

	private Node treeNode;
	private ProcessStages doNext;
	private Node next;
	private Iterator<Node> childrenCurNodeIter;
	private Iterator<Node> childrenSubNodeIter;

	public NodeIterator(Node treeNode) {
		this.treeNode = treeNode;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = treeNode.fils.iterator();
	}


	@Override
	public boolean hasNext() {

		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.treeNode;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				Node childDirect = childrenCurNodeIter.next();
				childrenSubNodeIter = childDirect.iterator();
				this.doNext = ProcessStages.ProcessChildSubNode;
				return hasNext();
			}

			else {
				this.doNext = null;
				return false;
			}
		}
		
		if (this.doNext == ProcessStages.ProcessChildSubNode) {
			if (childrenSubNodeIter.hasNext()) {
				this.next = childrenSubNodeIter.next();
				return true;
			}
			else {
				this.next = null;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return hasNext();
			}
		}

		return false;
	}

	@Override
	public Node next() {
		return this.next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
