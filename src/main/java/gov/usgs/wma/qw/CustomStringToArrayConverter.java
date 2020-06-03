package gov.usgs.wma.qw;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomStringToArrayConverter implements Converter<String, String[]>{
	@Override
	public String[] convert(String source) {
		return StringUtils.delimitedListToStringArray(source, ";");
	}

}
