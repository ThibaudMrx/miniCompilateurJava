/**
 * 
 */
package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.BlockSemanticsError;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a cell in an array.
 * @author Marc Pantel
 */
public class ArrayAssignment extends AbstractArray implements AssignableExpression {

	/**
	 * Construction for the implementation of an array element assignment expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element assignment expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element assignment expression.
	 */
	public ArrayAssignment(AssignableExpression _array, Expression _index) {
		super(_array, _index);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.ArrayAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		VariableDeclaration vardec;
		if (this.array instanceof VariableAssignment) {
			//on cast pour pouvoir accéder au registre et à l'offset de la declaration de variable
			vardec = ((VariableAssignment) this.array).getDeclaration();
			//récupération de l'adresse du registre où est stocké l'adresse du tableau
			_fragment.add(_factory.createLoadA(vardec.getRegister(), vardec.getOffset()));
			//récupération de l'adresse du tableau
			_fragment.add(_factory.createLoadI(1));
			//calcul de l'adresse à accéder par rapport à l'adresse du tableau
			_fragment.append(this.index.getCode(_factory));
			int taille = this.array.getType().length();
			_fragment.add(_factory.createLoadL(taille));
			_fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
			_fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
			//récupération du bon élément dans le tableau
			_fragment.add(_factory.createStoreI(taille));
			return _fragment;
		} else {
			throw new BlockSemanticsError("mauvais type pour ArrayAssignment");
		}
		
	}

	
}
