/**
 * 
 */
package api.options;

/**
 * @author Loris
 *
 */
public class AfterOption extends AbstractParameteredOption<Long> {

    private long value;
    
    protected AfterOption(){
	super("after");
    }

    @Override
    public void set(Long parameter) {
	value = parameter;
    }

    @Override
    public Long get() {
	return value;
    }
    
    public long getAfter(){
	return get();
    }

}
