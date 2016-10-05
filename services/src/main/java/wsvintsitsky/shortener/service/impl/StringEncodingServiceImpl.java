package wsvintsitsky.shortener.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.service.StringEncodingService;

@Service
public class StringEncodingServiceImpl implements StringEncodingService {

	private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	public String encodeString(String string) {
		double id1 = (double)(string.hashCode() + (double)(new Date().hashCode()) +  (double)3*2147483647);
		int z;
		StringBuilder sb = new StringBuilder();
		while(id1 > 0) {
			z = (int) (id1%62);
			char ch = CHARS.charAt(z);
			sb.append(ch);
			id1 = (id1 - z)/62;
		}
		return sb.toString();
	}

	
}
