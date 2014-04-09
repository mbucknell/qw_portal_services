package gov.usgs.cida.qw.code;

import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;

/** 
 * An extension of Code which provides a list of Providers. This list will be sorted alphabetically and duplicates will be
 * eliminated. 
 * @author drsteini
 *
 */
public class AggregatedCode extends Code {

    private static final long serialVersionUID = 7174466649683434137L;

    protected TreeSet<String> providers;
    
    public AggregatedCode(Code inCode, String inProvider) {
        if (null == inCode) {
            throw new IllegalArgumentException("Must provide the Code to use this constructor.");
        }
        setValue(inCode.getValue());
        setDesc(inCode.getDesc());
        addProvider(inProvider);
    }
    
    public AggregatedCode() {}

    @XmlElement
    public String getProviders() {
        return providers.toString().replace("[", "").replace(",", "").replace("]", "");
    }

    public void addProvider(final String inProvider) {
        if (null == inProvider || 0 == inProvider.length()) {
            throw new IllegalArgumentException("The provider cannot be empty.");
        }
        if (null == providers) {
            providers = new TreeSet<>();
        }
        providers.add(inProvider.toUpperCase());
    }

}
