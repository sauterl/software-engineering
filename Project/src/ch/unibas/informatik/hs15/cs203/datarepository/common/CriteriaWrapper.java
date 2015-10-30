package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;

/**
 * The {@link Criteria} wrapper. Since the api classes must not be changed, this
 * wrapper class provides extended access to the {@link Criteria} object.
 * 
 * @author Loris
 * @see Criteria
 */
public class CriteriaWrapper {

	private final Criteria wrapped;

	public CriteriaWrapper(final Criteria toWrapp) {
		wrapped = toWrapp;
	}

	public CriteriaWrapper(final String id) {
		this(Criteria.forId(id));
	}

	public CriteriaWrapper(final String nameOrNull, final String textOrNull,
			final Date afterOrNull, final Date beforeOrNull) {
		this(new Criteria(nameOrNull, textOrNull, afterOrNull, beforeOrNull));
	}

	/**
	 * Compares this object and another one on equality. Eclipse generated.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CriteriaWrapper)) {
			return false;
		}
		final CriteriaWrapper other = (CriteriaWrapper) obj;
		if (getAfter() == null) {
			if (other.getAfter() != null) {
				return false;
			}
		} else if (!getAfter().equals(other.getAfter())) {
			return false;
		}
		if (getBefore() == null) {
			if (other.getBefore() != null) {
				return false;
			}
		} else if (!getBefore().equals(other.getBefore())) {
			return false;
		}
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		if (getText() == null) {
			if (other.getText() != null) {
				return false;
			}
		} else if (!getText().equals(other.getText())) {
			return false;
		}
		return true;
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getAfter()
	 */
	public Date getAfter() {
		return wrapped.getAfter();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getBefore()
	 */
	public Date getBefore() {
		return wrapped.getBefore();
	}

	public String getId() {
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
	 * Returns a hash value for this object. The hash value method is eclipse
	 * generated.
	 * 
	 * @return A hash value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getAfter() == null) ? 0 : getAfter().hashCode());
		result = prime * result
				+ ((getBefore() == null) ? 0 : getBefore().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result
				+ ((getText() == null) ? 0 : getText().hashCode());
		return result;
	}

	/**
	 * Returns true if this is an empty criteria, so all fields are set to null.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return getId() == null && getName() == null && getText() == null
				&& getBefore() == null && getAfter() == null;
	}

	/**
	 * Returns true if this is a criteria query for a certain id. In other
	 * words: returns true if and only if <tt>getId() == null</tt>
	 * 
	 * @return
	 */
	public boolean isIdCriteria() {
		return getId() == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
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
}
