package com.servicepro.API;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPCall
{
		public String GET (String url)
		{
			String result = null;

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url(url).build();

			Response response = null;

			try
			{
				response = client.newCall(request).execute();
				result = response.body().string();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return result;
		}

		public String POST(String url, RequestBody requestBody)
		{
			String result = null;

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(url)
					.post(requestBody)
					.build();

			Response response = null;

			try {
				response = client.newCall(request).execute();
				result = response.body().string();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return result;
		}
}
