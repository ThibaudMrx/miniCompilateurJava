/**
 * 
 */
package fr.n7.stl.block.ast.instruction.declaration;

import java.util.*;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.FunctionType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a function declaration.
 * @author Marc Pantel
 */
public class ClassDeclaration implements Instruction, Declaration {

	protected TypeDeclaration retour;
	
	protected SymbolTable _newScope;
	
	/**
	 * Name of the class
	 */
	protected String name;
	
	

	/**
	 * AST node for the body of the function
	 */
	protected Block body;

	/**
	 * Builds an AST node for a function declaration
	 * @param _name : Name of the function
	 * @param _type : AST node for the returned type of the function
	 * @param _parameters : List of AST nodes for the formal parameters of the function
	 * @param _body : AST node for the body of the function
	 */
	public ClassDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		this.name = _name;
		this.body = _body;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.body.toString();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getType()
	 */
	@Override
	//TODO
	public Type getType() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		_scope.register(this);
		if (_newScope == null) {
			_newScope = new SymbolTable(_scope);
		}
		for (ParameterDeclaration p : this.parameters) {
			_newScope.register(p);
		}
		List<Type> paramType = new LinkedList<Type>();
		for (ParameterDeclaration p : this.parameters) {
			paramType.add(p.getType());
		}
		this.retour = new TypeDeclaration("return", new FunctionType(this.type, paramType));
		_newScope.register(this.retour);
		boolean ok = this.body.collect(_newScope);
		return ok;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return (this.body.resolve(_newScope)) && (this.type.resolve(_newScope));
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		//on passera dans le CheckType du return qui vérifiera le type de retour
		return this.body.checkType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		int count = 3;
		for (ParameterDeclaration p : this.getParameters()) {
			count += p.getType().length();
		}
		this.body.allocateMemory(_register.LB, count+1);
		for (ParameterDeclaration p : this.getParameters()) {
			p.offset = count;
			count -= p.getType().length();
		}
		return 0;
		//throw new SemanticsUndefinedException( "Semantics allocateMemory is undefined in FunctionDeclaration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = this.body.getCode(_factory);
		_fragment.addPrefix(this.name);
		_fragment.add(_factory.createLoadL(0));
		int tailleparam = 0;
		for (ParameterDeclaration p : this.getParameters()) {
			tailleparam += p.type.length();
		}
		_fragment.add(_factory.createReturn(this.type.length(), tailleparam));
		return _fragment;
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in FunctionDeclaration.");
	}

}
