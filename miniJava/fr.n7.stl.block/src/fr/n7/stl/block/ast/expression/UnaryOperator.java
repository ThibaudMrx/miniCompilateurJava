/**
 * 
 */
package fr.n7.stl.block.ast.expression;

/**
 * @author Marc Pantel
 *
 */
public enum UnaryOperator {
	
	/**
	 * Boolean negation
	 */
	Negate,
	/**
	 * Numeric opposite
	 */
	Opposite,
	/**
	 * Space Allocation
	 */
	Malloc,
	/**
	 * Integer Print
	 */
	PrintInt,
	/**
	 * Integer Print
	 */
	PrintChar,
	/**
	 * Integer Print
	 */
	PrintStr,
	/**
	 * Integer Print
	 */
	PrintBool;

	@Override
	public String toString() {
		switch (this) {
		case Negate: return "!";
		case Opposite: return "-";
		case Malloc: return "Malloc ";
		case PrintInt: return "Print Integer : ";
		case PrintChar: return "Print Character : ";
		case PrintStr: return "Print String : ";
		case PrintBool: return "Print Boolean : ";
		default: throw new IllegalArgumentException( "The default case should never be triggered.");		
		}
	}

}
