package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Comparator;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

class MetaDataComparator implements Comparator<MetaData> {
	@Override
	public int compare(MetaData md1, MetaData md2) {
		return md1.getTimestamp().compareTo(md2.getTimestamp());
	}
}
