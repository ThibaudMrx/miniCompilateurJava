/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import java.util.Optional;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		boolean b1 = this.condition.collect(_scope);
		boolean b2 = this.thenBranch.collect(_scope);
		if (this.elseBranch == null) {
			return b1 && b2;
		} else {
			boolean b3 = this.elseBranch.collect(_scope);
			return b1 && b2 && b3;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean b1 = this.condition.resolve(_scope);
		boolean b2 = this.thenBranch.resolve(_scope);
		if (this.elseBranch == null) {
			return b1 && b2;
		} else {
			boolean b3 = this.elseBranch.resolve(_scope);
			return b1 && b2 && b3;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean b1 = this.condition.getType().compatibleWith(AtomicType.BooleanType);
		boolean b2 = this.thenBranch.checkType();
		if (this.elseBranch == null) {
			return b1 && b2;
		} else {
			boolean b3 = this.elseBranch.checkType();
			return b1 && b2 && b3;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int num_cond = _factory.createLabelNumber();
		Fragment _fragment = this.condition.getCode(_factory);
		_fragment.add(_factory.createJumpIf("else_cond_"+ num_cond, 0));
		
		_fragment.append(this.thenBranch.getCode(_factory));
		_fragment.add(_factory.createJump("fin_cond_"+ num_cond));
		
		_fragment.addSuffix("else_cond_" + num_cond);
		if (this.elseBranch != null) {
			_fragment.append(this.elseBranch.getCode(_factory));
		}
		_fragment.addSuffix("fin_cond_" + num_cond);
		return _fragment;
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in Conditional.");
	}

}