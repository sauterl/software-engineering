package api;

public interface CallbackInterface {

	/**
	 * @return total number of bytes to be processed. If no number has been set, 0 will be returned.
	 */
	public int getTotalBytes();
	
	/**
	 * @param bytes Set total number of bytes to be processed. Total number of bytes must be >0
	 * @throws Exception If bytes is <0
	 */
	public void setTotalBytes(int bytes) throws Exception;
	
	/**
	 * @return number of already processed bytes. If no number has been set, 0 will be returned
	 */
	public int getProcessedBytes();
	
	/**
	 * Set number of bytes already processed
	 * @param bytes must be >0
	 * @throws Exception If param is <0
	 */
	public void setProcessedBytes(int bytes) throws Exception;
	
}
