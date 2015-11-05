/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

/**
 * The manual page object represents the entry of a command in the manual.
 * 
 * Note that all methods return print-ready strings.
 * @author Loris
 *
 */
public interface ManualPageEntry {
	
	/**
	 * Returns the title of this {@link ManualPageEntry}.
	 * @return The title of this {@link ManualPageEntry}.
	 */
	public String getTitle();
	
	/**
	 * Returns the synopsis of this {@link ManualPageEntry}.
	 * @return The synopsis of this {@link ManualPageEntry}.
	 */
	public String getSynopsis();
	
	/**
	 * Returns the 'parameters and options' section.
	 * @return The paramters and options section.
	 */
	public String getParamNOptions();
	
	/**
	 * Returns the description section.
	 * @return The description section.
	 */
	public String getDescription();

}
