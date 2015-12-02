package datarepository;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

/**
 * The dummy progress listener simply ignores passed progress information.
 * 
 * @author Loris
 * 
 */
class DummyProgressListener implements ProgressListener {

	/**
	 * Creates a new default {@link DummyProgressListener}.
	 */
	public DummyProgressListener() {
	}

	/**
	 * Finishes this {@link ProgressListener}.
	 * 
	 * @see ProgressListener#finish()
	 */
	@Override
	public void finish() {

	}

	/**
	 * The overridden method does ignore the passed information.
	 * 
	 * @param numberOfBytes
	 *            The number of bytes already processed.
	 * @param totalNumberOfBytes
	 *            The total number of bytes to process.
	 * @see ProgressListener#progress(long, long)
	 */
	@Override
	public void progress(final long numberOfBytes, final long totalNumberOfBytes) {

	}

	/**
	 * Starts this {@link ProgressListener}. The overridden method simply does
	 * nothing.
	 * 
	 * @see ProgressListener#start()
	 */
	@Override
	public void start() {

	}

	@Override
	public boolean hasCancelBeenRequested() {
		return false;
	}

	@Override
	public void canceled() {
	}

}
