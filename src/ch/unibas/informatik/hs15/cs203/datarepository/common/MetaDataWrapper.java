package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * Wrapper for {@link MetaData}
 *
 * @author Loris
 *
 */
public class MetaDataWrapper {

	private final MetaData wrapped;

	public MetaDataWrapper(final MetaData meta) {
		wrapped = meta;
	}

	public MetaDataWrapper(final String id, final String name,
			final String description, final int numberOfFiles, final long size,
			final Date timestamp) {
		this(new MetaData(id, name, description, numberOfFiles, size,
				timestamp));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (isThis(obj)) {
			return true;
		} else if (isNull(obj)) {
			return false;
		} else {
			if (isInstanceOf(obj)) {
				final MetaDataWrapper o = (MetaDataWrapper) obj;
				final boolean id = isEqual(getId(), o.getId());
				final boolean name = isEqual(getName(), o.getName());
				final boolean desc = isEqual(getDescription(),
						o.getDescription());
				final boolean no = getNumberOfFiles() == o.getNumberOfFiles();
				final boolean size = getSize() == o.getSize();
				final boolean time = isEqual(getTimestamp(), o.getTimestamp());
				return id && name && desc && no && size && time;

			}
			return false;

		}
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getDescription()
	 */
	public String getDescription() {
		return wrapped.getDescription();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getId()
	 */
	public String getId() {
		return wrapped.getId();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getName()
	 */
	public String getName() {
		return wrapped.getName();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getNumberOfFiles()
	 */
	public int getNumberOfFiles() {
		return wrapped.getNumberOfFiles();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getSize()
	 */
	public long getSize() {
		return wrapped.getSize();
	}

	/**
	 * @return
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getTimestamp()
	 */
	public Date getTimestamp() {
		return wrapped.getTimestamp();
	}

	public MetaData getWrappedObject() {
		return wrapped;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result
				+ ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getDescription() == null) ? 0
				: getDescription().hashCode());
		result = prime * result
				+ ((getNumberOfFiles() == 0) ? 0 : getNumberOfFiles());
		result = prime * result + ((getSize() == 0) ? 0 : (int) getSize());
		result = prime * result
				+ ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
		return result;
	}

	private boolean isEqual(final Object o1, final Object o2) {
		boolean out = false;
		if (o1 == null) {
			out = o2 == null;
		} else {
			out = o1.equals(o2);
		}
		return out;
	}

	private boolean isInstanceOf(final Object o) {
		return o instanceof MetaDataWrapper;
	}

	private boolean isNull(final Object o) {
		return o == null;
	}

	private boolean isThis(final Object o) {
		return this == o;
	}

}
