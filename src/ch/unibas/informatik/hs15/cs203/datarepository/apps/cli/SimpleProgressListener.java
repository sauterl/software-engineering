package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

class SimpleProgressListener implements ProgressListener{

	@Override
	public void start() {
	System.out.println("Starter copying");
	
	}

	@Override
	public void progress(long numberOfBytes, long totalNumberOfBytes) {
	System.out.print("copied "+(double)numberOfBytes/(double)totalNumberOfBytes+" Percent\r");
	
	}

	@Override
	public void finish() {
	System.out.println("\nfinished seccessfully");
	
	}

	@Override
	public boolean hasCancelBeenRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub
		
	}

}
