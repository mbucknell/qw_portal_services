package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeList;
import gov.usgs.cida.qw.codes.CodeType;
import gov.usgs.cida.qw.codes.dao.CodeDao;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

public abstract class CodesRestController extends BaseRestController {

	protected CodeDao codeDao;

	protected CodeList getList(final CodeType codeType, final String text, final String inPageNumber, final String inPageSize,
			final Map<String, Object> addlParms, WebRequest webRequest) {
		CodeList codes = new CodeList();
		if (isNotModified(webRequest)) {
			return null;
		} else {
			HashMap<String, Object> queryParams = new HashMap<>();
			if (null != addlParms) {
				queryParams.putAll(addlParms);
			}

			if (StringUtils.hasText(text)) {
				try {
					queryParams.put("text", URLDecoder.decode(text, BaseRestController.DEFAULT_ENCODING));
				} catch (UnsupportedEncodingException e) {
					throw new AssertionError("UTF-8 not supported");
				}
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
			codes.setCodes(codeDao.getCodes(codeType, queryParams));
			codes.setRecordCount(codeDao.getRecordCount(codeType, queryParams));
			return codes;
		}
	}

	protected Code getCode(final CodeType codeType, final String codeValue, WebRequest webRequest, HttpServletResponse response) {
		Code rtn = null;
		if (!isNotModified(webRequest)) {
			try {
				rtn = codeDao.getCode(codeType, URLDecoder.decode(codeValue, BaseRestController.DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				throw new AssertionError("UTF-8 not supported");
			}
			if (null == rtn) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
			}
		}
		return rtn;
	}
	
	public static Boolean isInteger(final String number) {
		if (!StringUtils.hasText(number)) {
			return false;
		}

		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

}
