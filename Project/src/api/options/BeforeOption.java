/**
 * 
 */
package api.options;

/**
 * @author Loris
 *
 */
public class BeforeOption extends AbstractParameteredOption<Long> {

    private long value;
    
    protected BeforeOption(){
	super("before");
    }

    @Override
    public void set(Long parameter) {
	this.value = parameter;
    }

    @Override
    public Long get() {
	return value;
    }
    
    public long getBefore(){
	return get();
    }

}
