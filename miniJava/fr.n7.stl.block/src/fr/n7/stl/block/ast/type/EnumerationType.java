/**
 * 
 */
package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.declaration.LabelDeclaration;
import fr.n7.stl.util.BlockSemanticsError;

/**
 * @author Marc Pantel
 *
 */
public class EnumerationType implements Type, Declaration {
	
	private String name;
	
	private List<LabelDeclaration> labels;

	/**
	 * 
	 */
	public EnumerationType(String _name, List<LabelDeclaration> _labels) {
		this.name = _name;
		this.labels = _labels;
		for (LabelDeclaration l : labels) {
			l.setEnumeration(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = "enum" + this.name + " { ";
		Iterator<LabelDeclaration> _iter = this.labels.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " }";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#equalsTo(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof EnumerationType) {
			//cast en EnumerationType
			EnumerationType enumOther = (EnumerationType) _other;
			return (this.labels.containsAll(enumOther.labels)) && (enumOther.labels.containsAll(this.labels));
		} else {
			throw new BlockSemanticsError("second type pas un enumtype");
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#compatibleWith(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		//compatibilité seulement entre types enumerations
		if (this.getType().equalsTo(_other)) {
			return true;
		} else {
			return false;
		}
		//throw new SemanticsUndefinedException("Semantics compatibleWith is not implemented in EnumerationType.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#merge(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public Type merge(Type _other) {
		return (_other).merge(this.getType());
		//throw new SemanticsUndefinedException("Semantics merge is not implemented in EnumerationType.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#length()
	 */
	@Override
	public int length() {
		//return this.labels.size();
		return 1;
		//throw new SemanticsUndefinedException("Semantics length is not implemented in EnumerationType.");
	}
	
	
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		for (LabelDeclaration l : this.labels) {
			_scope.register(l);
		}
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this;
	}
	
	public int getIndex(String label) {
		int i = 0;
		//variable qui contiendra l'index du label cherché ou -1 sinon
		int c = -1;
		for (LabelDeclaration l : this.labels) {
			String labelname = l.getName();
			if (label.equals(labelname)) {
				//label trouvé
				c = i;
			}
			i++;
		}
		//label inconnu
		if (c == -1 ) {
			throw new BlockSemanticsError("label inconnu pour cette énumération");
		} else {
			return c;
		}
	}

}
