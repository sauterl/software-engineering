package util.logging;

import util.jsontools.Json;

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
    
    private boolean validateString(Json jsonParent, ConfigDefinition.Element definition){
	if(definition.getType() != ConfigDefinition.Element.Type.STRING){
	    //definition's type is NOT string
	    throw new IllegalArgumentException("The ConfigDefinition.Element type must be STRING.");
	}
	//definition's type is string.
	if(jsonParent.containsEntry(definition.getKey() )){
	    // jsonParent does contain the specified key.
	    return true;
	}else{
	    // does not contain expected key
	    return false;
	}
    }
    
    private boolean validateBoolean(Json jsonParent, ConfigDefinition.Element definition){
	if(definition.getType() != ConfigDefinition.Element.Type.BOOLEAN){
	    //definition's type is NOT string
	    throw new IllegalArgumentException("The ConfigDefinition.Element type must be BOOLEAN.");
	}
	// unsupported
	throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private boolean validateArray(Json jsonParent, ConfigDefinition.Element definition){
	if(definition.getType() != ConfigDefinition.Element.Type.ARRAY){
	    //definition's type is NOT string
	    throw new IllegalArgumentException("The ConfigDefinition.Element type must be ARRAY.");
	}
	if(jsonParent.containsSet(definition.getKey() )){
	    //contains key, key refers to set (=array)
	    Json[] entries = jsonParent.getSet(definition.getKey() );
	    for(Json json : entries){
		
	    }
	    return false;
	}else{
	    return false;
	}
    }
    
    private boolean validateObject(Json jsonObj, ConfigDefinition.Element element){
	if(element.getType() != ConfigDefinition.Element.Type.OBJECT){
	    return false;
	}
	for(ConfigDefinition.Element sub: element.children){
	    switch(sub.type){
	    case ARRAY:
		break;
	    case BOOLEAN:
		break;
	    case OBJECT:
		break;
	    case STRING:
		break;
	    default:
		break;
	    
	    }
	}
	return false;
    }
    
    private boolean validate(Json config, ConfigDefinition def){
	// TODO validate config for def
	for(ConfigDefinition.Element elmnt : def.contents){
	    if(!config.containsEntry(elmnt.getKey())){
		return false;
	    }
	    switch(elmnt.type){
	    case ARRAY:
		
		break;
	    case BOOLEAN:
		// unsupported
		break;
	    case OBJECT:
		break;
	    case STRING:
		
		break;
	    default:
		break;
	    
	    }
	}
	return false;
    }
    
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
	
	public String getVersion(){
	    return version;
	}
	
	public Element[] getContents(){
	    return contents;
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
	    
	    public boolean isRequired(){
		return required;
	    }
	    
	    public String getKey(){
		return key;
	    }
	    
	    public Type getType(){
		return type;
	    }
	    
	    public Type getChildrenType(){
		return childrenType;
	    }
	    
	    public Element getChildrenDefinition(){
		return childrenDef;
	    }
	    
	    public Element[] getChildren(){
		return children;
	    }
	}
    }

}
