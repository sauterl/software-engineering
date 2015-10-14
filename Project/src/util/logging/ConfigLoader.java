package util.logging;


@Deprecated
public class ConfigLoader {
    
    private static ConfigLoader instance = null;
    
    private ConfigLoader() {
	// singleton
    }
    
    public static ConfigLoader getConfigLoader(){
	if(instance == null){
	    instance = new ConfigLoader();
	}
	return instance;
    }
    
    @SuppressWarnings("unused")
	private static class ConfigDefinition{
	
	static final ConfigDefinition devConfig = new ConfigDefinition("dev 0.1", Element.VERSION,
		Element.HANDLERS,
		Element.LOGGERS
		);
	
	
	private final String version;
	private final Element[] contents;
	
	public ConfigDefinition(String version, Element...contents){
	    this.version = version;
	    this.contents = new Element[contents.length];
	    for(int i=0; i<contents.length; i++){
		this.contents[i] = contents[i];
	    }
	}
	
	static class Element{
	    static final Element VERSION = new Element("version", Element.Type.STRING, true);
		static final Element LEVEL = new Element("level", Element.Type.STRING, true);
		static final Element REFERENCE = new Element("ref", Element.Type.STRING, true);
		static final Element NAME = new Element("name", Element.Type.STRING, true);
		static final Element HANDLER = new Element("handler", Element.Type.STRING, true);
		static final Element HANDLER_OBJECT = new Element(null, Element.Type.OBJECT, true, null, REFERENCE, NAME, LEVEL);
		static final Element HANDLERS = new Element("handlers", Element.Type.ARRAY, true, Element.Type.OBJECT, HANDLER_OBJECT);
		static final Element LOGGER_OBJECT = new Element(null, Element.Type.OBJECT, true, null, NAME, HANDLER, LEVEL);
		static final Element LOGGERS = new Element("loggers", Element.Type.ARRAY, true, Element.Type.OBJECT, LOGGER_OBJECT);
	    static enum Type{
		STRING,
		ARRAY,
		OBJECT,
		BOOLEAN
	    }
	    private final String key;
	    private final Type type;
	    private final boolean required;
	    private final Type childrenType;
	    private final Element childrenDef;
	    private final Element[] children;
	    
	    public Element(String key, Type type, boolean required){
		this(key, type, required, null, null);
	    }
	    
	    public Element(String key, Type type, boolean required, Type childrenType, Element childrenDef, Element...children){
		this.key = key;
		this.type = type;
		this.required = required;
		this.childrenDef = childrenDef;
		this.childrenType = childrenType;
		this.children = new Element[children.length];
		for(int i=0; i<children.length; i++){
		    this.children[i] = children[i];
		}
	    }
	    
	    public String getKey(){
		return key;
	    }
	    
	    public Type getType(){
		return type;
	    }
	}
    }

}
