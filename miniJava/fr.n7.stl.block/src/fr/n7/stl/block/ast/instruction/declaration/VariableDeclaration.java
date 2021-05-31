/**
 * 
 */
package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.SequenceType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.BlockSemanticsError;

/**
 * Abstract Syntax Tree node for a variable declaration instruction.
 * @author Marc Pantel
 *
 */
public class VariableDeclaration implements Declaration, Instruction {

	/**
	 * Name of the declared variable.
	 */
	protected String name;
	
	/**
	 * AST node for the type of the declared variable.
	 */
	protected Type type;
	
	/**
	 * AST node for the initial value of the declared variable.
	 */
	protected Expression value;
	
	/**
	 * Address register that contains the base address used to store the declared variable.
	 */
	protected Register register;
	
	/**
	 * Offset from the base address used to store the declared variable
	 * i.e. the size of the memory allocated to the previous declared variables
	 */
	protected int offset;
	
	/**
	 * Creates a variable declaration instruction node for the Abstract Syntax Tree.
	 * @param _name Name of the declared variable.
	 * @param _type AST node for the type of the declared variable.
	 * @param _value AST node for the initial value of the declared variable.
	 */
	public VariableDeclaration(String _name, Type _type, Expression _value) {
		this.name = _name;
		this.type = _type;
		this.value = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.type + " " + this.name + " = " + this.value + ";\n";
	}

	/**
	 * Synthesized semantics attribute for the type of the declared variable.
	 * @return Type of the declared variable.
	 */
	public Type getType() {
		//System.out.println(this.name + " : " + this.type.toString());
		return this.type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.block.ast.VariableDeclaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Synthesized semantics attribute for the register used to compute the address of the variable.
	 * @return Register used to compute the address where the declared variable will be stored.
	 */
	public Register getRegister() {
		return this.register;
	}
	
	/**
	 * Synthesized semantics attribute for the offset used to compute the address of the variable.
	 * @return Offset used to compute the address where the declared variable will be stored.
	 */
	public int getOffset() {
		return this.offset;
	}
	
	/**
	 * Synthesized semantics attribute for the expression of the declaration
	 * @return Expression of the declared variable.
	 */
	public Expression getValue() {
		return this.value;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		_scope.register(this);
		//System.out.println("table : " + _scope.toString());
		boolean ok1 = this.getType().collect(_scope);
		boolean ok2 = this.getValue().collect(_scope);
		return ok1 && ok2;
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in VariableDeclaration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean ok1 = this.getType().resolve(_scope);
		boolean ok2 = this.getValue().resolve(_scope);
		return ok1 && ok2;
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in VariableDeclaration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Type treal = this.getValue().getType();
		//System.out.println(this.getType().toString() + " : " + this.getValue().getType().toString());
		return this.getType().compatibleWith(treal);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;
		if (this.type instanceof NamedType) {
			NamedType typecast1 = (NamedType) this.type;
			if (typecast1.getType() instanceof RecordType) {
				RecordType typecast2 = (RecordType) typecast1.getType();
				//System.out.println("AllocateMemory à finir dans VariableDeclaration");
				typecast2.AllocateMemory(_register, _offset);
			}
		}
		return this.type.length();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		_fragment.add(_factory.createPush(this.type.length()));
		_fragment.append(this.value.getCode(_factory));
		_fragment.add(_factory.createStore(this.register, this.offset, this.getType().length()));
		return _fragment;
		//throw new SemanticsUndefinedException("Semantics getCode is undefined in VariableDeclaration.");
	}

}
