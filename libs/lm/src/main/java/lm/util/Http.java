package lm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by limin on 15/09/11.
 */
public class Http {
	private String mUrl;

	public Http(String url) {
		mUrl = url;
	}

	public String request(Map<String, String> headers, String method, String data) {
		method = method.toUpperCase();

		HttpURLConnection conn = null;

		StringBuffer requestData = new StringBuffer();

		try{
			// 建立请求连接
			conn = (HttpURLConnection) new URL(mUrl).openConnection();

			// 设置请求方法
			conn.setRequestMethod(method);
			if("POST".equals(method)) {
				conn.setDoOutput(true);
				conn.setDoInput(true);
			}

			// 设置头信息
			if(headers != null) {
				Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			// 设置POST
			if("POST".equals(method) && data != null && data.length() != 0) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				writer.append(data);
				writer.flush();
				writer.close();
			}

			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				boolean isGzip = false;

				Iterator<Map.Entry<String, List<String>>> it = conn.getHeaderFields().entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, List<String>> entry = it.next();
					isGzip = entry.getValue().contains("gzip");
					if(isGzip) break;
				}

				InputStream in = isGzip ? new GZIPInputStream(conn.getInputStream()) : conn.getInputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				char[] buf = new char[2048];
				int len;
				while((len = reader.read(buf)) != -1) {
					requestData.append(buf, 0, len);
				}

				reader.close();
				in.close();
			}
		}
		catch(MalformedURLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if(conn != null) {
				conn.disconnect();
			}
		}

		if(requestData.length() == 0) {
			return null;
		}

		return requestData.toString();
	}
}
