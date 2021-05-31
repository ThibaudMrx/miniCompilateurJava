/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.block.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.BlockSemanticsError;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected TargetType target;
	protected Type type;
	protected String name;

	public AbstractConversion(TargetType _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(TargetType _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		if (this.type != null) {
			return this.type;
		} else  {
			throw new BlockSemanticsError("erreur affectation type de la conversion");
		}
		//throw new SemanticsUndefinedException("Semantics getType undefined in TypeConversion.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		if (this.type == null) {
			Declaration decl = _scope.get(this.name);
			//on affecte le type grâce à la table des symboles
			this.type = decl.getType();
		}
		boolean ok;
		if (this.target instanceof Expression) {
			//cast de target
			Expression targetExpr = (Expression) this.target;
			ok = targetExpr.collect(_scope) && this.type.collect(_scope);
			return ok;
		} else {
			throw new BlockSemanticsError("conversion vers qqchose qui n'est pas un type (Collect)");
		}

		//throw new SemanticsUndefinedException("Semantics collect undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean ok;
		if (this.target instanceof Expression) {
			//cast de target
			Expression targetExpr = (Expression) this.target;
			//on peut alors faire les resolve
			ok = targetExpr.resolve(_scope) && this.type.resolve(_scope) && (targetExpr.getType().compatibleWith(this.type));
			return ok;			
		} else {
			throw new BlockSemanticsError("conversion vers qqchose qui n'est pas un type (Resolve)");
		}
		//throw new SemanticsUndefinedException("Semantics resolve undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		if (this.target instanceof Expression) {
			//cast de target
			Expression targetExpr = (Expression) this.target;
			_fragment.append(targetExpr.getCode(_factory));
			if (targetExpr instanceof IdentifierAccess) {
				_fragment.add(_factory.createLoadI(this.type.length()));
			}		
		}
		return _fragment;
		//throw new SemanticsUndefinedException("Semantics getCode undefined in TypeConversion.");
	}

}
