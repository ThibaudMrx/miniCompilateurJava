/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractPointer;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a pointer access expression.
 * @author Marc Pantel
 *
 */
public class PointerAccess extends AbstractPointer implements Expression {

	/**
	 * Construction for the implementation of a pointer content access expression Abstract Syntax Tree node.
	 * @param _pointer Abstract Syntax Tree for the pointer expression in a pointer content access expression.
	 */
	public PointerAccess(Expression _pointer) {
		super(_pointer);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		//récupération de l'adresse à laquelle est stockée l'adresse de la valeur pointée.
		Fragment _fragment = this.pointer.getCode(_factory);
		//récupération de l'adresse de la valeur pointée
		_fragment.add(_factory.createLoadI(1));
		//récupération de la valeur pointée
		_fragment.add(_factory.createLoadI(this.pointer.getType().length()));
		return _fragment;
		//throw new SemanticsUndefinedException("Semantics getCode is not implemented in PointerAccess.");
	}

}
