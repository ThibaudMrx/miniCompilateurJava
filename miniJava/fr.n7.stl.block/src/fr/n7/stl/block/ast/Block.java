/**
 * 
 */
package fr.n7.stl.block.ast;

import java.util.List;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Represents a Block node in the Abstract Syntax Tree node for the Bloc language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 * 
 * @author Marc Pantel
 *
 */
public class Block {

	private static int nbInstances; //attribut de classe qui permet de savoir combien d'instances de Block ont été crées
	
	private int numBlock; //Atribut qui permet de savoir quel est le numéro de cette instance
	
	private int dep;
	
	private int offset_init;
	/**
	 * Sequence of instructions contained in a block.
	 */
	protected List<Instruction> instructions;
	
	protected HierarchicalScope<Declaration> _newScope;

	/**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> _instructions) {
		this.instructions = _instructions;
		this.numBlock = this.nbInstances;
		this.nbInstances++;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n" ;
	}
	
	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and check
	 * that the declaration are allowed.
	 * @param _scope Inherited Scope attribute that contains the identifiers defined previously
	 * in the context.
	 * @return Synthesized Semantics attribute that indicates if the identifier declaration are
	 * allowed.
	 */
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (_newScope == null) {
			_newScope = new SymbolTable(_scope);
		}
		for (Instruction i : this.instructions) {
			ok = ok && i.collect(_newScope);
		}
		return ok;
		//throw new SemanticsUndefinedException("Semantics collect is undefined in Block.");
	}
	
	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined and
	 * associate all identifiers uses with their definitions.
	 * @param _scope Inherited Scope attribute that contains the defined identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used in the
	 * block have been previously defined.
	 */
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		for (Instruction i : this.instructions) {
			ok = ok && i.resolve(_newScope);
		}
		return ok;
		//throw new SemanticsUndefinedException("Semantics resolve is undefined in Block.");
	}

	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */	
	public boolean checkType() {
		boolean ok = true;
		for (Instruction i : this.instructions) {
			ok = ok && i.checkType();
		}
		return ok;
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated memory. 
	 * @param _register Inherited Register associated to the address of the variables.
	 * @param _offset Inherited Current offset for the address of the variables.
	 */	
	public void allocateMemory(Register _register, int _offset) {
		offset_init = _offset;
		dep = _offset;
		for (Instruction i : this.instructions) {
			dep += i.allocateMemory(_register,  dep);
		}
	}

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * @param _factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */
	public Fragment getCode(TAMFactory _factory) {
		Fragment _result = _factory.createFragment();
		//System.out.println("numBlock--------------------------->" + this.numBlock);
		for (Instruction i : this.instructions) {
			_result.append(i.getCode(_factory));
		}
		_result.add(_factory.createPop(0, this.dep - this.offset_init));
		//le bloc global du code porte le numéro de bloc le plus élevé
		if (this.numBlock == this.nbInstances - 1) {
			_result.add(_factory.createHalt());
		}
		return _result;
		//throw new SemanticsUndefinedException("Semantics generateCode is undefined in Block.");
	}

}
