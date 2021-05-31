/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing an array element.
 * @author Marc Pantel
 *
 */
public class ArrayAccess extends AbstractArray implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(Expression _array, Expression _index) {
		super(_array,_index);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		//récupération de l'adresse du registre où est stocké l'adresse du tableau
		_fragment.append(this.array.getCode(_factory));
		//récupération de l'adresse du tableau
		_fragment.add(_factory.createLoadI(1));
		//calcul de l'adresse à accéder par rapport à l'adresse du tableau
		_fragment.append(this.index.getCode(_factory));
		int taille = this.array.getType().length();
		_fragment.add(_factory.createLoadL(taille));
		_fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
		_fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		//récupération du bon élément dans le tableau
		_fragment.add(_factory.createLoadI(taille));
		return _fragment;
		
		//throw new SemanticsUndefinedException( "getCode is undefined in ArrayAccess.");
	}

}
