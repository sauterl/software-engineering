package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;

/**
 * The {@link Criteria} wrapper.
 * Since the api classes must not be changed, this wrapper class
 * provides extended access to the {@link Criteria} object.
 * @author Loris
 * @see Criteria
 */
public class CriteriaWrapper {
	
	private Criteria wrapped;

	public CriteriaWrapper(String nameOrNull, String textOrNull, Date afterOrNull,
	          Date beforeOrNull) {
		this(new Criteria(nameOrNull, textOrNull, afterOrNull, beforeOrNull));
	}
	
	public CriteriaWrapper(Criteria toWrapp){
		wrapped = toWrapp;
	}
	
	public CriteriaWrapper(String id){
		this(Criteria.forId(id));
	}
	
	public String getId(){
		return wrapped.getId();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getName()
	 */
	public String getName() {
		return wrapped.getName();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getText()
	 */
	public String getText() {
		return wrapped.getText();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getBefore()
	 */
	public Date getBefore() {
		return wrapped.getBefore();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getAfter()
	 */
	public Date getAfter() {
		return wrapped.getAfter();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CriteriaWrapper [");
		if (getId() != null) {
			builder.append("getId()=");
			builder.append(getId());
			builder.append(", ");
		}
		if (getName() != null) {
			builder.append("getName()=");
			builder.append(getName());
			builder.append(", ");
		}
		if (getText() != null) {
			builder.append("getText()=");
			builder.append(getText());
			builder.append(", ");
		}
		if (getBefore() != null) {
			builder.append("getBefore()=");
			builder.append(getBefore());
			builder.append(", ");
		}
		if (getAfter() != null) {
			builder.append("getAfter()=");
			builder.append(getAfter());
		}
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Returns a hash value for this object.
	 * The hash value method is eclipse generated.
	 * @return A hash value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAfter() == null) ? 0 : getAfter().hashCode());
		result = prime * result + ((getBefore() == null) ? 0 : getBefore().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
		return result;
	}

	/**
	 * Compares this object and another one on equality.
	 * Eclipse generated.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CriteriaWrapper))
			return false;
		CriteriaWrapper other = (CriteriaWrapper) obj;
		if (getAfter() == null) {
			if (other.getAfter() != null)
				return false;
		} else if (!getAfter().equals(other.getAfter()))
			return false;
		if (getBefore() == null) {
			if (other.getBefore() != null)
				return false;
		} else if (!getBefore().equals(other.getBefore()))
			return false;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getText() == null) {
			if (other.getText() != null)
				return false;
		} else if (!getText().equals(other.getText()))
			return false;
		return true;
	}

}
