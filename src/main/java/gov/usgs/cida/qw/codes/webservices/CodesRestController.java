package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeList;
import gov.usgs.cida.qw.codes.CodeType;
import gov.usgs.cida.qw.codes.dao.CodeDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

public abstract class CodesRestController extends BaseRestController {
	
	protected CodeDao codeDao;

    protected CodeList getList(final CodeType codeType, final String text, final String inPageNumber, final String inPageSize,
    		final Map<String, Object> addlParms, WebRequest webRequest) {
        if (isNotModified(webRequest)) {
            return null;
        } else {
	        HashMap<String, Object> queryParams = new HashMap<>();
	        if (null != addlParms) {
	        	queryParams.putAll(addlParms);
	        }

	        if (StringUtils.isNotEmpty(text)) {
	            queryParams.put("text", text);
	        }

	    	if (isInteger(inPageSize) && Integer.parseInt(inPageSize) > 0) {
	    		//We will repsect a fetchsize if given.
	    		Integer fetchSize = Integer.parseInt(inPageSize);
		    	queryParams.put("fetchSize", fetchSize);

	    		if (isInteger(inPageNumber) && Integer.parseInt(inPageNumber) > 0) {
	    			// But the page number is only respected when provided with a fetchsize
	    			Integer pageNumber = Integer.parseInt(inPageNumber);
	    			queryParams.put("offset", (pageNumber - 1) * fetchSize);
	    		}
	    	}
	        CodeList codes = new CodeList();
	        codes.setCodes(codeDao.getCodes(codeType, queryParams));
	        codes.setRecordCount(codeDao.getRecordCount(codeType, queryParams));
	        return codes;
        }
    }

    protected Code getCode(final CodeType codeType, final String codeValue, WebRequest webRequest, HttpServletResponse response) {
    	Code rtn = null;
        if (!isNotModified(webRequest)) {
        	rtn = codeDao.getCode(codeType, codeValue);
        	if (null == rtn) {
        		response.setStatus(HttpStatus.NOT_FOUND.value());
        	}
        }
        return rtn;
    }
    
    public static Boolean isInteger(final String number) {
        if (StringUtils.isBlank(number)) {
        	return false;
        }

        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    protected Map<String, Object> processRequestParam(final String key, final String[] param) {
    	Map<String, Object> queryParams = new HashMap<>();
    	ArrayList<String> splitParam = new ArrayList<>();
    	if (null != param) {
    		for (int i=0; i<param.length; i++) {
    			if (StringUtils.isNotBlank(param[i])) {
    				splitParam.addAll(Arrays.asList(param[i].split(";")));
    			}
    		}
    		if (splitParam.size() > 0) {
    			queryParams.put(key, splitParam.toArray(new String[splitParam.size()]));
    		}
    	}
    	return queryParams;
    }

}
