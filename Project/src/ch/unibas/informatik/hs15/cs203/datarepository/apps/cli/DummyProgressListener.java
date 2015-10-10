package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

public class DummyProgressListener implements ProgressListener{

    public DummyProgressListener() {
    }

    @Override
    public void start() {
	
    }

    @Override
    public void progress(long numberOfBytes, long totalNumberOfBytes) {
	
    }

    @Override
    public void finish() {
	
    }

}
