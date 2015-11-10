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

	/**
	 * Creates a CriteriaWrapper object with all values set to <tt>null</tt>
	 *
	 * @return A CriteriaWrapper object to retain all meta data.
	 * @see Criteria#all()
	 */
	public static CriteriaWrapper all() {
		return new CriteriaWrapper(Criteria.all());
	}

	/**
	 * Creates a CriteriaWrapper object for the given ID.
	 *
	 * @param id
	 *            The ID, a not null, non-empty string.
	 * @return A CriteriaWrapper object for the given ID.
	 * @see Criteria#forId(String)
	 */
	public static CriteriaWrapper forId(final String id) {
		return new CriteriaWrapper(Criteria.forId(id));
	}

	/**
	 * Wrapped object
	 */
	private final Criteria wrapped;

	/**
	 * Creates a CriteriaWrapper for the given Criteria.
	 *
	 * @param toWrapp
	 *            The Criteria Object to wrap
	 * @see Criteria
	 */
	public CriteriaWrapper(final Criteria toWrapp) {
		wrapped = toWrapp;
	}

	/**
	 * Creates a CirteriaWrapper object for the given ID.
	 *
	 * @param id
	 *            The ID, a not null, non-empty string.
	 * @see Criteria#forId(String)
	 */
	public CriteriaWrapper(final String id) {
		this(Criteria.forId(id));
	}

	/**
	 * Creates a new CriteriaWrapper object with the given arguments.
	 *
	 * @param nameOrNull
	 *            The name of the data set to match
	 * @param textOrNull
	 *            A text snippet which the data set's name or description must
	 *            contain
	 * @param afterOrNull
	 *            The time stamp of all matching data sets has to be after this
	 *            time stamp.
	 * @param beforeOrNull
	 *            The time stamp of all matching data sets has to be before this
	 *            time stamp.
	 * @see Criteria#Criteria(String, String, Date, Date)
	 */
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
		/*
		 * Exception driven check if members are equal
		 * Note: check* means exception when false, otherwise nothing
		 * is* means returns boolean
		 */
		if (isThis(obj)) {
			return true;
		} else if (isNull(obj)) {
			return false;
		} else {
			if (isInstanceOf(obj)) {
				final CriteriaWrapper o = (CriteriaWrapper) obj;
				try {
					checkAfter(o);
					checkBefore(o);
					checkName(o);
					checkText(o);
					checkID(o);
					return true;
				} catch (final RuntimeException e) {

				}
			}
			return false;
		}

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

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria#getId()
	 */
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

	public Criteria getWrappedObject() {
		return wrapped;
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

	public boolean isNull() {
		return wrapped == null;
	}

	/**
	 * Returns true if this is a criteria query for a certain id. In other
	 * words: returns true if and only if <tt>getId() != null</tt> and all other
	 * getters return <tt>null</tt>
	 *
	 * @return
	 */
	public boolean onlyID() {
		return getId() != null && (getAfter() == null && getBefore() == null
				&& getName() == null && getText() == null);
	}

	/**
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

	private void checkAfter(final CriteriaWrapper other) {
		if (getAfter() == null) {
			if (other.getAfter() != null) {
				throwRExc();
			}
		} else {
			if (!getAfter().equals(other.getAfter())) {
				throwRExc();
			}
		}
	}

	private void checkBefore(final CriteriaWrapper other) {
		if (getBefore() == null) {
			if (other.getBefore() != null) {
				throwRExc();
			}
		} else {
			if (!getBefore().equals(other.getBefore())) {
				throwRExc();
			}
		}
	}

	private void checkID(final CriteriaWrapper other) {
		if (getId() == null) {
			if (other.getId() != null) {
				throwRExc();
			}
		} else {
			if (!getId().equals(other.getId())) {
				throwRExc();
			}
		}
	}

	private void checkName(final CriteriaWrapper other) {
		if (getName() == null) {
			if (other.getName() != null) {
				throwRExc();
			}
		} else {
			if (!getName().equals(other.getName())) {
				throwRExc();
			}
		}
	}

	private void checkText(final CriteriaWrapper other) {
		if (getText() == null) {
			if (other.getText() != null) {
				throwRExc();
			}
		} else {
			if (!getText().equals(other.getText())) {
				throwRExc();
			}
		}
	}

	private boolean isInstanceOf(final Object obj) {
		return obj instanceof CriteriaWrapper;
	}

	private boolean isNull(final Object obj) {
		return obj == null;
	}

	private boolean isThis(final Object obj) {
		return this == obj;
	}

	private void throwRExc() {
		throw new RuntimeException();
	}
}
