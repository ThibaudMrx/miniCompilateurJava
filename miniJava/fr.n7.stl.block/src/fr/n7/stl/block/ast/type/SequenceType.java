/**
 * 
 */
package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/**
 * Implementation of the Abstract Syntax Tree node for a sequence type.
 * @author Marc Pantel
 *
 */
public class SequenceType implements Type {
	
	private List<Type> types;

	public SequenceType() {
		this.types = new LinkedList<Type>();
	}
	
	public void add(Type _type) {
		this.types.add(_type);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		Type target = _other;
		if (target instanceof NamedType) {
			target = ((NamedType) target).getType();					
		}
		if (target instanceof SequenceType) {
			SequenceType _local = (SequenceType) target;
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				boolean _result = true;
				while (i1.hasNext() && i2.hasNext() && _result) {
					_result = _result && (i1.next().equalsTo(i2.next()));
				}
				return _result;
			} else {
				return false;
			}
		} else {
			if (target instanceof ArrayType) {
				boolean _result = true;
				Type _element = ((ArrayType)target).getType();
				Iterator<Type> _iter = this.types.iterator();
				while (_iter.hasNext() && _result) {
					_result = _result && _iter.next().equalsTo(_element);
				}
				return _result;
			} else {
				if (target instanceof RecordType) {
					return this.equalsTo(((RecordType)target).erase());
				} else {
					if (target instanceof CoupleType) {
						if (this.types.size() == 2) {
							return this.types.get(0).equalsTo(((CoupleType)target).getFirst()) &&
									this.types.get(1).equalsTo(((CoupleType)target).getSecond());
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		Type target = _other;
		if (target instanceof NamedType) {
			target = ((NamedType) target).getType();					
		}
		if (target instanceof SequenceType) {
			SequenceType _local = (SequenceType) target;
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				boolean _result = true;
				while (i1.hasNext() && i2.hasNext() && _result) {
					_result = _result && (i1.next().compatibleWith(i2.next()));
				}
				return _result;
			} else {
				return false;
			}
		} else {
			if (target instanceof ArrayType) {
				boolean _result = true;
				Type _element = ((ArrayType)target).getType();
				Iterator<Type> _iter = this.types.iterator();
				while (_iter.hasNext() && _result) {
					_result = _result && _iter.next().equalsTo(_element);
				}
				return _result;
			} else {
				if (target instanceof RecordType) {
					return this.compatibleWith(((RecordType)target).erase());
				} else {
					if (target instanceof CoupleType) {
						if (this.types.size() == 2) {
							return this.types.get(0).compatibleWith(((CoupleType)target).getFirst()) &&
									this.types.get(1).compatibleWith(((CoupleType)target).getSecond());
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		Type target = _other;
		if (target instanceof NamedType) {
			target = ((NamedType) target).getType();					
		}
		if (target instanceof SequenceType) {
			SequenceType _local = (SequenceType) target;
			SequenceType _result = new SequenceType();
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				while (i1.hasNext() && i2.hasNext()) {
					_result.add(i1.next().merge(i2.next()));
				}
				return _result;
			} else {
				return AtomicType.ErrorType;
			}
		} else {
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		int _result = 0;
		for (Type _type : this.types) {
			_result += _type.length();
		}
		return _result;
	}
	
	
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean _result = true;
		for (Type t : this.types) {
			_result = _result && t.resolve(_scope);
		}
		return _result;
	}
}
