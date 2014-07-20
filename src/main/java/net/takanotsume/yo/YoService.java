package net.takanotsume.yo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ByteArrayEntity;

import net.takanotsume.yo.YoFailedException;

/**
 * Execute Yo Api
 * @author Akihito Tanaka
 */
public class YoService {
	
	/* URL : Yo all subscribers */
	private static String URL_YO_ALL = "http://api.justyo.co/yoall/";
	
	/* URL : Yo individual users*/
	private static String URL_YO = "http://api.justyo.co/yo/";
	
	/* parameter key : api token */
	private static String PARAM_KEY_API_TOKEN = "api_token";
	
	/* parameter key : username */
	private static String PARAM_KEY_USERNAME = "username";
	
	/* header value : content-type */
	private static String HEADER_CONTENT_TYPE = "application/x-www-form-urlencoded";
	
	/**
	 * Send a Yo to all subscribers
	 * @param token
	 * @throws YoFailedException
	 */
	public static void sendYoAll(String token) throws YoFailedException {
		
		List<NameValuePair> param = Form.form().add(PARAM_KEY_API_TOKEN, token).build();
		doPost(URL_YO_ALL, param);
		
	}
	
	/**
	 * Send a Yo to individual username
	 * @param token
	 * @param username
	 * @throws YoFailedException
	 */
	public static void sendYo(String token, String username) throws YoFailedException {
		
		List<NameValuePair> param = Form.form().add(PARAM_KEY_API_TOKEN, token)
											   .add(PARAM_KEY_USERNAME, username)
											   .build();
		doPost(URL_YO, param);
	}
	
	private static void doPost(String url, List<NameValuePair> param) throws YoFailedException {
		
		Response response = null;
		OutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			response = Request.Post(url)
								.bodyForm(param)
								.addHeader("Content-Type", HEADER_CONTENT_TYPE)
								.execute();
			
			HttpResponse httpResponse = response.returnResponse();
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if (statusCode != 201) {
				((ByteArrayEntity)httpResponse.getEntity()).writeTo(outputStream);
				throw new YoFailedException("Sending Yo was Failed. statusCode : " + statusCode 
																    + " message : " + outputStream.toString());
			}
			
		} catch (IOException e) {
			throw new YoFailedException("Sending Yo was Failed. Exception thrown : " + e.getMessage(), e);
		} finally {
			if (response != null) {
				response.discardContent();
			}
			
			IOUtils.closeQuietly(outputStream);
			
		}
	}
	
}
