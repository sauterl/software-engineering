package api;

/**
 * The {@link CallbackInterface} receives progress updates.
 * 
 * Classes which implement this interface must handle these updates on their
 * own. They may get never published.
 * 
 * @author Loris
 * 
 */
public interface CallbackInterface {

    /**
     * Returns the total number of bytes to be processed.
     * 
     * In case no number has been set previously by calling
     * {@link CallbackInterface#setTotalBytes(int)}, 0 is returned.
     * 
     * @return total number of bytes to be processed. If no number has been set,
     *         0 will be returned.
     */
    public int getTotalBytes();

    /**
     * Sets the total number of bytes to be processed.
     * 
     * This number must be a positive integer or otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param bytes
     *            Set total number of bytes to be processed. Total number of
     *            bytes must be a positive integer, otherwise a
     *            {@link IllegalArgumentException} will be thrown.
     * @throws IllegalArgumentException
     *             If the {@code bytes} argument is a negative integer.
     */
    public void setTotalBytes(int bytes) throws IllegalArgumentException;

    /**
     * Returns the number of already processed bytes.
     * 
     * In case no number has been set previously by invoking
     * {@link CallbackInterface#setProcessedBytes(int)}, 0 is returned.
     * 
     * @return number of already processed bytes. If no number has been set, 0
     *         will be returned.
     */
    public int getProcessedBytes();

    /**
     * Set number of bytes already processed.
     * 
     * This number must be a positive integer or otherwise an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param bytes
     *            The number of bytes already processed. Must be a positive
     *            integer or a {@link IllegalArgumentException} will be thrown.
     * 
     * @throws IllegalArgumentException
     *             If the {@code bytes} argument is a negative integer.
     */
    public void setProcessedBytes(int bytes) throws IllegalArgumentException;

}
