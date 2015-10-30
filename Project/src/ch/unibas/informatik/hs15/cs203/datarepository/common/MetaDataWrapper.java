package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * Wrapper for {@link MetaData}
 * @author Loris
 *
 */
public class MetaDataWrapper {
	
	private MetaData wrapped;

	public MetaDataWrapper(MetaData meta) {
		wrapped = meta;
	}

	public MetaDataWrapper(String id, String name, String description, int numberOfFiles, long size,
          Date timestamp){
		this (new MetaData(id, name, description, numberOfFiles, size, timestamp));
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
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData#getDescription()
	 */
	public String getDescription() {
		return wrapped.getDescription();
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
		result = prime * result + ((getNumberOfFiles() == 0) ? 0 : getNumberOfFiles());
		result = prime * result + ((getSize() == 0) ? 0 : (int)getSize() );
		result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MetaDataWrapper))
			return false;
		MetaDataWrapper other = (MetaDataWrapper) obj;
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
		if (getDescription() == null) {
			if (other.getDescription() != null) {
				return false;
			}
		} else if (!getDescription().equals(other.getDescription())) {
			return false;
		}
		if (getNumberOfFiles() == 0) {
			if (other.getNumberOfFiles() != 0) {
				return false;
			}
		} else if (getNumberOfFiles() != other.getNumberOfFiles()) {
			return false;
		}
		if (getSize() == 0) {
			if (other.getSize() != 0) {
				return false;
			}
		} else if (getSize() != other.getSize()) {
			return false;
		}
		if (getTimestamp() == null) {
			if (other.getTimestamp() != null) {
				return false;
			}
		} else if (!getTimestamp().equals(other.getTimestamp())) {
			return false;
		}
		return true;
	}
	
	
}
