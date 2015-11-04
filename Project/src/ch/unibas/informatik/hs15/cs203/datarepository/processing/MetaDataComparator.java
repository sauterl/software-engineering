package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Comparator;

import ch.unibas.informatik.hs15.cs203.datarepository.common.MetaDataWrapper;

class MetaDataComparator implements Comparator<MetaDataWrapper> {
	@Override
	public int compare(MetaDataWrapper md1, MetaDataWrapper md2) {
		return md1.getTimestamp().compareTo(md2.getTimestamp());
	}
}
